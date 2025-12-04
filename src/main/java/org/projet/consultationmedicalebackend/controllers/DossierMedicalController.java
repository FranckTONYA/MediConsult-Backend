package org.projet.consultationmedicalebackend.controllers;

import org.projet.consultationmedicalebackend.models.*;
import org.projet.consultationmedicalebackend.services.*;
import org.projet.consultationmedicalebackend.utils.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/dossier")
@CrossOrigin(origins = "*")
public class DossierMedicalController {

    @Autowired
    private DossierMedicalService dossierMedicalService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private PatientService patientService;

    @PostMapping
    public ResponseEntity<DossierMedical> createDossier(@RequestBody DossierMedical dossier) {
        DossierMedical nouveau = dossierMedicalService.save(dossier);
        return ResponseEntity.ok(nouveau);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<DossierMedical>> getAllDossiers() {
        List<DossierMedical> dossiers = dossierMedicalService.findAll();
        return ResponseEntity.ok(dossiers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DossierMedical> getDossierById(@PathVariable Long id) {
        Optional<DossierMedical> dossier = dossierMedicalService.findById(id);
        return dossier.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/find-by-patient/{idPatient}")
    public Optional<DossierMedical>getDossierByPatient(@PathVariable Long idPatient) {
        return dossierMedicalService.findByPatient(idPatient);
    }

    @GetMapping("/find-by-medecin/{idMedecin}")
    public ResponseEntity<List<DossierMedical>>getDossierByMedecin(@PathVariable Long idMedecin) {
        List<DossierMedical> dossierMedicals = dossierMedicalService.findByMedecin(idMedecin);
        return ResponseEntity.ok(dossierMedicals);
    }

    @PutMapping("/update/{id}")
    public DossierMedical updateDossier(@PathVariable Long id, @RequestBody DossierMedical dossierMedical) {
        dossierMedical.setId(id);
        return dossierMedicalService.save(dossierMedical);
    }

    @PutMapping(value = "/update-with-files/{id}", consumes = "multipart/form-data")
    public ResponseEntity<?> updateWithFiles(
            @PathVariable Long id,
            @RequestPart("dossierMedical") DossierMedical dossierMedical,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        try {
            DossierMedical dossierFound = dossierMedicalService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Dossier médical non trouvée"));

            // Mise à jour
            dossierFound.setDate(LocalDateTime.now());
            dossierMedicalService.save(dossierFound);

            // Sauvegarde des fichiers
            if (files != null) {
                for (MultipartFile file : files) {

                    if (file.isEmpty()) continue;

                    String folder = "/uploads-dossiersMedicaux/";

                    CustomResponse response = fileStorageService.saveFile(file, folder);

                    if(!response.status)
                        return ResponseEntity.badRequest().body(Map.of("error", response.message));

                    String generatedName = response.message;

                    TypeDoc type = fileStorageService.detectFileType(file);

                    Document doc = new Document(
                            generatedName,
                            type,
                            generatedName,
                            dossierFound
                    );
                    documentService.save(doc);
                }
            }
            return ResponseEntity.ok(Map.of("message", "Dossier médical mise à jour avec succès"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body((Map.of("error", "Erreur serveur :" + e.getMessage())));
        }
    }

    @DeleteMapping("/delete-by-id/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<DossierMedical> dossierOpt = dossierMedicalService.findById(id);

        if (dossierOpt.isPresent()) {
            DossierMedical dossierMedical = dossierOpt.get();

            // Suppression des documents liés
            if (dossierMedical.getDocuments() != null) {
                for (Document doc : dossierMedical.getDocuments()) {
                    // Supprime le fichier physique
                    File f = new File(System.getProperty("user.dir") + "/uploads-dossiersMedicaux/" + doc.getUrlStockage());
                    if (f.exists()) f.delete();

                    // Supprime l'enregistrement en DB
                    documentService.delete(doc.getId());
                }
            }

            // Supprime l'ordonnance
            dossierMedicalService.delete(id);

            return ResponseEntity.ok(Map.of("message", "Dossier médical et ses documents supprimés avec succès"));
        } else {
            return ResponseEntity.status(404).body(Map.of("error", "Dossier médical introuvable"));
        }
    }
}
