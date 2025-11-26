package org.projet.consultationmedicalebackend.services;

import org.projet.consultationmedicalebackend.models.AnalyseLabo;

import java.util.List;
import java.util.Optional;

public interface AnalyseLaboService {
    AnalyseLabo save(AnalyseLabo analyseLabo);
    List<AnalyseLabo> findAll();
    Optional<AnalyseLabo> findById(Long id);
    List<AnalyseLabo> findByPatient(Long patientId);
    void delete(Long id);
}
