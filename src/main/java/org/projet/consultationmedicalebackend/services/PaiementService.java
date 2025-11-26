package org.projet.consultationmedicalebackend.services;

import org.projet.consultationmedicalebackend.models.Paiement;

import java.util.List;
import java.util.Optional;

public interface PaiementService {
    Paiement save(Paiement paiement);
    List<Paiement> findAll();
    Optional<Paiement> findById(Long id);
    List<Paiement> findByPatient(Long patientId);
    void delete(Long id);
}
