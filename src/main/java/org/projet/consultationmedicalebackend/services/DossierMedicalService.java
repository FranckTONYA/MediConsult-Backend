package org.projet.consultationmedicalebackend.services;

import org.projet.consultationmedicalebackend.models.DossierMedical;

import java.util.List;
import java.util.Optional;

public interface DossierMedicalService {
    DossierMedical save(DossierMedical dossierMedical);
    List<DossierMedical> findAll();
    Optional<DossierMedical> findById(Long id);
    Optional<DossierMedical> findByPatient(Long patientId);
    List<DossierMedical> findByMedecin(Long medecinId);
    void delete(Long id);
}
