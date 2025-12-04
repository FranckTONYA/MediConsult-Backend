package org.projet.consultationmedicalebackend.controllers;

import org.projet.consultationmedicalebackend.models.Consentement;
import org.projet.consultationmedicalebackend.services.ConsentementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/consentement")
@CrossOrigin(origins = "*")
public class ConsentementController {

    @Autowired
    private ConsentementService consentementService;

    @PostMapping
    public ResponseEntity<Consentement> save(@RequestBody Consentement consentement) {
        Consentement nouveau = consentementService.save(consentement);
        return ResponseEntity.ok(nouveau);
    }

    @PostMapping("/create")
    public ResponseEntity<Consentement> createConsent(@RequestBody Map<String, Long> data) {

        Long patientId = data.get("patientId");
        Long medecinId = data.get("medecinId");

        Consentement c = consentementService.createConsent(patientId, medecinId);
        return ResponseEntity.ok(c);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Consentement>> getAll() {
        List<Consentement> consentements = consentementService.findAll();
        return ResponseEntity.ok(consentements);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Consentement> getDossierById(@PathVariable Long id) {
        Optional<Consentement> consentement = consentementService.findById(id);
        return consentement.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/find-by-medecin/{medecinId}")
    public ResponseEntity<List<Consentement>>getDossierByMedecin(@PathVariable Long medecinId) {
        List<Consentement> consentements = consentementService.findByMedecin(medecinId);
        return ResponseEntity.ok(consentements);
    }

    @GetMapping("/find-by-patient/{patientId}")
    public ResponseEntity<List<Consentement>>getDossierByPatient(@PathVariable Long patientId) {
        List<Consentement> consentements = consentementService.findByPatient(patientId);
        return ResponseEntity.ok(consentements);
    }

    @PutMapping("/update/{id}")
    public Consentement update(@PathVariable Long id, @RequestBody Consentement consentement) {
        consentement.setId(id);
        return consentementService.save(consentement);
    }

    @PutMapping("/update-status/{id}")
    public ResponseEntity<Consentement> updateStatus(@PathVariable Long id,
                                                     @RequestBody Map<String, String> payload) {

        String statut = payload.get("statut");
        return ResponseEntity.ok(consentementService.updateStatus(id, statut));
    }

    @DeleteMapping("/delete-by-id/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        consentementService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
