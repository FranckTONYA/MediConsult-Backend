package org.projet.consultationmedicalebackend.controllers;

import org.projet.consultationmedicalebackend.models.Paiement;
import org.projet.consultationmedicalebackend.services.PaiementService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/paiement")
@CrossOrigin(origins = "*")
public class PaiementController {

    private final PaiementService paiementService;

    public PaiementController(PaiementService paiementService) {
        this.paiementService = paiementService;
    }

    @GetMapping("/getAll")
    public List<Paiement> getAll() {
        return paiementService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Paiement> getById(@PathVariable Long id) {
        return paiementService.findById(id);
    }

    @GetMapping("/find-by-patient/{patientId}")
    public List<Paiement> getByPatient(@PathVariable Long patientId) {
        return paiementService.findByPatient(patientId);
    }

    @PostMapping
    public Paiement create(@RequestBody Paiement paiement) {
        return paiementService.save(paiement);
    }

    @PutMapping("/update/{id}")
    public Paiement update(@PathVariable Long id, @RequestBody Paiement paiement) {
        paiement.setId(id);
        return paiementService.save(paiement);
    }

    @DeleteMapping("/delete-by-id/{id}")
    public void delete(@PathVariable Long id) {
        paiementService.delete(id);
    }
}
