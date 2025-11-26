package org.projet.consultationmedicalebackend.repositories;

import org.projet.consultationmedicalebackend.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    List<Consultation> findByPatient(Patient patient);
    List<Consultation> findByMedecin(Medecin medecin);
}
