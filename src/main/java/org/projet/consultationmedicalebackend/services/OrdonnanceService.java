package org.projet.consultationmedicalebackend.services;

import org.projet.consultationmedicalebackend.models.Ordonnance;
import java.util.List;
import java.util.Optional;

public interface OrdonnanceService {
    Ordonnance save(Ordonnance ordonnance);
    List<Ordonnance> findAll();
    Optional<Ordonnance> findById(Long id);
    List<Ordonnance> findByPatient(Long patientId);
    List<Ordonnance> findByMedecin(Long medecinId);
    void delete(Long id);
}
