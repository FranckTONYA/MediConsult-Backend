package org.projet.consultationmedicalebackend.repositories;

import org.projet.consultationmedicalebackend.models.Consentement;
import org.projet.consultationmedicalebackend.models.Medecin;
import org.projet.consultationmedicalebackend.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsentementRepository extends JpaRepository<Consentement, Long> {
    List<Consentement> findByMedecin_Id(Long medecinId);
    List<Consentement> findByPatient_Id(Long patientId);
}
