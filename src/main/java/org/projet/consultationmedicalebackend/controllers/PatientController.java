package org.projet.consultationmedicalebackend.controllers;

import org.projet.consultationmedicalebackend.models.Medecin;
import org.projet.consultationmedicalebackend.models.Patient;
import org.projet.consultationmedicalebackend.services.PatientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/patient")
@CrossOrigin(origins = "*")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/getAll")
    public List<Patient> getAll() {
        return patientService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Patient> getById(@PathVariable Long id) {
        return patientService.findById(id);
    }

    @GetMapping("/find-by-email/{email}")
    public Optional<Patient> getByEmail(@PathVariable String email) {
        return patientService.findByEmail(email);
    }

    @GetMapping("/find-by-niss/{niss}")
    public Optional<Patient> getByNISS(@PathVariable String niss) {
        return patientService.findByNiss(niss);
    }

    @PutMapping("/update/{id}")
    public Patient update(@PathVariable Long id, @RequestBody Patient patient) {
        patient.setId(id);
        return patientService.save(patient);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        patientService.delete(id);
    }

    @GetMapping("/get-medecins-of-patient/{patientId}")
    public List<Medecin> getMedecinsOfPatient(@PathVariable Long patientId) {
        Patient patient = patientService.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient non trouvé"));

        return patient.getMedecins();
    }

}
