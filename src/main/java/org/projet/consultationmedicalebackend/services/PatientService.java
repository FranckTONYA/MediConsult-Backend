package org.projet.consultationmedicalebackend.services;

import org.projet.consultationmedicalebackend.models.Consultation;
import org.projet.consultationmedicalebackend.models.Patient;

import java.util.List;
import java.util.Optional;

public interface PatientService {
    Patient save(Patient patient);
    List<Patient> findAll();
    Optional<Patient> findById(Long id);
    Optional<Patient> findByEmail(String email);
    Optional<Patient> findByNiss(String niss);
    Optional<Patient> findByDossier(Long dossierId);
    void delete(Long id);
}
