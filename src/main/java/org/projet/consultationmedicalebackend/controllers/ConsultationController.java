package org.projet.consultationmedicalebackend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.projet.consultationmedicalebackend.models.*;
import org.projet.consultationmedicalebackend.services.ConsultationService;
import org.projet.consultationmedicalebackend.services.DocumentService;
import org.projet.consultationmedicalebackend.services.FileStorageService;
import org.projet.consultationmedicalebackend.utils.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/consultation")
@CrossOrigin(origins = "*")
public class ConsultationController {

    @Autowired
    private ConsultationService consultationService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private DocumentService documentService;

    @PostMapping
    public ResponseEntity<Consultation> sauvegarderConsultation(@RequestBody Consultation consultation) {
        Consultation nouvelle = consultationService.save(consultation);
        return ResponseEntity.ok(nouvelle);
    }

    @PostMapping("/creer-consultation")
    public ResponseEntity<?> creerConsultation(@RequestBody Consultation consultation) {
        CustomResponse response = consultationService.createConsultation(consultation);
        if (response.status)
            return ResponseEntity.ok(Map.of("message", response.message));
        else
            return ResponseEntity.badRequest().body(Map.of("error", response.message));
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<?> cancelConsultation(@PathVariable Long id) {
        try {
            CustomResponse response = consultationService.cancelOrRefuseConsultation(id, StatutRDV.ANNULER);
            if (response.status)
                return ResponseEntity.ok(Map.of("message", response.message));
            else
                return ResponseEntity.badRequest().body(Map.of("error", response.message));

        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Erreur serveur :" + e.getMessage()));
        }
    }

    @PutMapping("/refuse/{id}")
    public ResponseEntity<?> refuseConsultation(@PathVariable Long id) {
        try {
            CustomResponse response = consultationService.cancelOrRefuseConsultation(id, StatutRDV.REFUSER);
            if (response.status)
                return ResponseEntity.ok(Map.of("message", response.message));
            else
                return ResponseEntity.badRequest().body(Map.of("error", response.message));

        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Erreur serveur :" + e.getMessage()));
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Consultation>> getAllConsultations() {
        List<Consultation> consultations = consultationService.findAll();
        return ResponseEntity.ok(consultations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Consultation> getConsultationById(@PathVariable Long id) {
        Optional<Consultation> consultation = consultationService.findById(id);
        return consultation.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/find-by-patient/{idPatient}")
    public ResponseEntity<List<Consultation>> getConsultationsByPatient(@PathVariable Long idPatient) {
        List<Consultation> consultations = consultationService.findByPatient(idPatient);
        return ResponseEntity.ok(consultations);
    }

    @GetMapping("/find-by-medecin/{idMedecin}")
    public ResponseEntity<List<Consultation>> getConsultationsByMedecin(@PathVariable Long idMedecin) {
        List<Consultation> consultations = consultationService.findByMedecin(idMedecin);
        return ResponseEntity.ok(consultations);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Consultation> updateConsultation(@PathVariable Long id, @RequestBody Consultation consultation) {
        consultation.setId(id);
        Consultation result = consultationService.save(consultation);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete-by-id/{id}")
    public ResponseEntity<Void> deleteConsultation(@PathVariable Long id) {
        consultationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/update-with-files/{id}", consumes = "multipart/form-data")
    public ResponseEntity<?> updateWithFiles(
            @PathVariable Long id,
            @RequestPart("consultation") Consultation consultation,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        try {
            Consultation consultationFound = consultationService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Consultation non trouvée"));

            // Mise à jour
            consultationFound.setStatut(consultation.getStatut());
            consultationFound.setCompteRendu(consultation.getCompteRendu());

            consultationService.save(consultationFound);

            // Sauvegarde des fichiers
            if (files != null) {
                for (MultipartFile file : files) {

                    if (file.isEmpty()) continue;

                    String folder = "/uploads-consultations/";

                    CustomResponse response = fileStorageService.saveFile(file, folder);

                    if(!response.status)
                        return ResponseEntity.badRequest().body(Map.of("error", response.message));

                    String generatedName = response.message;

                    TypeDoc type = fileStorageService.detectFileType(file);

                    Document doc = new Document(
                            generatedName,
                            type,
                            generatedName,
                            consultationFound
                    );
                    documentService.save(doc);
                }
            }

            return ResponseEntity.ok(Map.of("message", "Consultation mise à jour avec succès"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body((Map.of("error", "Erreur serveur :" + e.getMessage())));
        }
    }

}
