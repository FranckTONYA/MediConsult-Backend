package org.projet.consultationmedicalebackend.controllers;

import org.projet.consultationmedicalebackend.models.AnalyseLabo;
import org.projet.consultationmedicalebackend.services.AnalyseLaboService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/analyse")
@CrossOrigin(origins = "*")
public class AnalyseLaboController {

    private final AnalyseLaboService analyseLaboService;

    public AnalyseLaboController(AnalyseLaboService analyseLaboService) {
        this.analyseLaboService = analyseLaboService;
    }

    @GetMapping("/getAll")
    public List<AnalyseLabo> getAll() {
        return analyseLaboService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<AnalyseLabo> getById(@PathVariable Long id) {
        return analyseLaboService.findById(id);
    }

    @GetMapping("/find-by-patient/{patientId}")
    public List<AnalyseLabo> getByPatient(@PathVariable Long patientId) {
        return analyseLaboService.findByPatient(patientId);
    }

    @PostMapping
    public AnalyseLabo create(@RequestBody AnalyseLabo analyseLabo) {
        return analyseLaboService.save(analyseLabo);
    }

    @PutMapping("/update/{id}")
    public AnalyseLabo update(@PathVariable Long id, @RequestBody AnalyseLabo analyseLabo) {
        analyseLabo.setId(id);
        return analyseLaboService.save(analyseLabo);
    }

    @DeleteMapping("/delate-by-id/{id}")
    public void delete(@PathVariable Long id) {
        analyseLaboService.delete(id);
    }
}
