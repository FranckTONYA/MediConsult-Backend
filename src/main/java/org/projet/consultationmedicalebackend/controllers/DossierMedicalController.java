package org.projet.consultationmedicalebackend.controllers;

import org.projet.consultationmedicalebackend.models.DossierMedical;
import org.projet.consultationmedicalebackend.services.DossierMedicalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/dossier")
@CrossOrigin(origins = "*")
public class DossierMedicalController {

    @Autowired
    private DossierMedicalService dossierMedicalService;

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

    @DeleteMapping("/delete-by-id/{id}")
    public ResponseEntity<Void> deleteDossier(@PathVariable Long id) {
        dossierMedicalService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
