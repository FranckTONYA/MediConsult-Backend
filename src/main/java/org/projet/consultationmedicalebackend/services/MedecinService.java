package org.projet.consultationmedicalebackend.services;

import org.projet.consultationmedicalebackend.models.Medecin;
import org.projet.consultationmedicalebackend.utils.CustomResponse;

import java.util.List;
import java.util.Optional;

public interface MedecinService {
    Medecin save(Medecin medecin);
    List<Medecin> findAll();
    Optional<Medecin> findById(Long id);
    Optional<Medecin> findByInami(String inami);
    Optional<Medecin> findByEmail(String email);
    List<Medecin> findBySpecialite(String specialite);
    void delete(Long id);
    CustomResponse assignPatientToMedecin(Long medecinId, Long patientId);
    CustomResponse removePatientFromMedecin(Long medecinId, Long patientId);
}
