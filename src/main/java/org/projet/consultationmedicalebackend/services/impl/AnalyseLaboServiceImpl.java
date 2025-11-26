package org.projet.consultationmedicalebackend.services.impl;

import org.projet.consultationmedicalebackend.models.AnalyseLabo;
import org.projet.consultationmedicalebackend.models.Patient;
import org.projet.consultationmedicalebackend.repositories.AnalyseLaboRepository;
import org.projet.consultationmedicalebackend.repositories.PatientRepository;
import org.projet.consultationmedicalebackend.services.AnalyseLaboService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnalyseLaboServiceImpl implements AnalyseLaboService {

    private final AnalyseLaboRepository analyseLaboRepository;
    private final PatientRepository patientRepository;

    public AnalyseLaboServiceImpl(AnalyseLaboRepository analyseLaboRepository, PatientRepository patientRepository) {
        this.analyseLaboRepository = analyseLaboRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public AnalyseLabo save(AnalyseLabo analyseLabo) {
        return analyseLaboRepository.save(analyseLabo);
    }

    @Override
    public List<AnalyseLabo> findAll() {
        return analyseLaboRepository.findAll();
    }

    @Override
    public Optional<AnalyseLabo> findById(Long id) {
        return analyseLaboRepository.findById(id);
    }

    @Override
    public List<AnalyseLabo> findByPatient(Long patientId) {
        Patient patient = patientRepository.findById(patientId).orElse(null);
        return analyseLaboRepository.findByPatient(patient);
    }

    @Override
    public void delete(Long id) {
        analyseLaboRepository.deleteById(id);
    }
}
