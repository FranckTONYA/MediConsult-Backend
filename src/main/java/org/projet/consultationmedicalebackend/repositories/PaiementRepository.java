package org.projet.consultationmedicalebackend.repositories;

import org.projet.consultationmedicalebackend.models.Paiement;
import org.projet.consultationmedicalebackend.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Long> {
    List<Paiement> findByPatient(Patient patient);
}
