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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/ordonnance")
@CrossOrigin(origins = "*")
public class OrdonnanceController {

    @Autowired
    private OrdonnanceService ordonnanceService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private MedecinService medecinService;

    @Autowired
    private PatientService patientService;

    public OrdonnanceController(OrdonnanceService ordonnanceService) {
        this.ordonnanceService = ordonnanceService;
    }

    @GetMapping("/getAll")
    public List<Ordonnance> getAll() {
        return ordonnanceService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Ordonnance> getById(@PathVariable Long id) {
        return ordonnanceService.findById(id);
    }

    @GetMapping("/find-by-patient/{patientId}")
    public List<Ordonnance> getByPatient(@PathVariable Long patientId) {
        return ordonnanceService.findByPatient(patientId);
    }

    @GetMapping("/find-by-medecin/{medecinId}")
    public List<Ordonnance> getByMedecin(@PathVariable Long medecinId) {
        return ordonnanceService.findByMedecin(medecinId);
    }

    @PostMapping
    public Ordonnance create(@RequestBody Ordonnance ordonnance) {
        return ordonnanceService.save(ordonnance);
    }

    @PostMapping(value = "/create-with-files", consumes = "multipart/form-data")
    public ResponseEntity<?> createWithFiles(
            @RequestPart("ordonnance") Ordonnance ordonnance,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        try {
            if (ordonnance.getMedecin() != null && ordonnance.getMedecin().getId() != null) {
                Medecin med = medecinService.findById(ordonnance.getMedecin().getId())
                        .orElseThrow(() -> new RuntimeException("Médecin introuvable"));
                ordonnance.setMedecin(med);
            }

            if (ordonnance.getPatient() != null && ordonnance.getPatient().getId() != null) {
                Patient pat = patientService.findById(ordonnance.getPatient().getId())
                        .orElseThrow(() -> new RuntimeException("Patient introuvable"));
                ordonnance.setPatient(pat);
            }
            ordonnance.setDate(LocalDate.now());
            ordonnanceService.save(ordonnance);

            // Sauvegarde des fichiers
            if (files != null) {
                for (MultipartFile file : files) {

                    if (file.isEmpty()) continue;

                    String folder = "/uploads-ordonnances/";

                    CustomResponse response = fileStorageService.saveFile(file, folder);

                    if(!response.status)
                        return ResponseEntity.badRequest().body(Map.of("error", response.message));

                    String generatedName = response.message;

                    TypeDoc type = fileStorageService.detectFileType(file);

                    Document doc = new Document(
                            generatedName,
                            type,
                            generatedName,
                            ordonnance
                    );
                    documentService.save(doc);
                }
            }
            return ResponseEntity.ok(Map.of("message", "Ordonnance créée avec succès"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body((Map.of("error", "Erreur serveur :" + e.getMessage())));
        }
    }

    @PutMapping(value = "/update-with-files/{id}", consumes = "multipart/form-data")
    public ResponseEntity<?> updateWithFiles(
            @PathVariable Long id,
            @RequestPart("ordonnance") Ordonnance ordonnance,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        try {
            Ordonnance ordonnanceFound = ordonnanceService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Ordonnance non trouvée"));

            // Mise à jour
            ordonnanceFound.setContenu(ordonnance.getContenu());
            ordonnanceService.save(ordonnanceFound);

            // Sauvegarde des fichiers
            if (files != null) {
                for (MultipartFile file : files) {

                    if (file.isEmpty()) continue;

                    String folder = "/uploads-ordonnances/";

                    CustomResponse response = fileStorageService.saveFile(file, folder);

                    if(!response.status)
                        return ResponseEntity.badRequest().body(Map.of("error", response.message));

                    String generatedName = response.message;

                    TypeDoc type = fileStorageService.detectFileType(file);

                    Document doc = new Document(
                            generatedName,
                            type,
                            generatedName,
                            ordonnanceFound
                    );
                    documentService.save(doc);
                }
            }
            return ResponseEntity.ok(Map.of("message", "Ordonnance mise à jour avec succès"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body((Map.of("error", "Erreur serveur :" + e.getMessage())));
        }
    }

    @PutMapping("/update/{id}")
    public Ordonnance update(@PathVariable Long id, @RequestBody Ordonnance ordonnance) {
        ordonnance.setId(id);
        return ordonnanceService.save(ordonnance);
    }

    @DeleteMapping("/delete-by-id/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Ordonnance> ordonnanceOpt = ordonnanceService.findById(id);

        if (ordonnanceOpt.isPresent()) {
            Ordonnance ordonnance = ordonnanceOpt.get();

            // Suppression des documents liés
            if (ordonnance.getDocuments() != null) {
                for (Document doc : ordonnance.getDocuments()) {
                    // Supprime le fichier physique
                    File f = new File(System.getProperty("user.dir") + "/uploads-ordonnances/" + doc.getUrlStockage());
                    if (f.exists()) f.delete();

                    // Supprime l'enregistrement en DB
                    documentService.delete(doc.getId());
                }
            }

            // Supprime l'ordonnance
            ordonnanceService.delete(id);

            return ResponseEntity.ok(Map.of("message", "Ordonnance et ses documents supprimés avec succès"));
        } else {
            return ResponseEntity.status(404).body(Map.of("error", "Ordonnance introuvable"));
        }
    }

}
