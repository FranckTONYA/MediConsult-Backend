package org.projet.consultationmedicalebackend.repositories;

import org.projet.consultationmedicalebackend.models.Medecin;
import org.projet.consultationmedicalebackend.models.Ordonnance;
import org.projet.consultationmedicalebackend.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdonnanceRepository extends JpaRepository<Ordonnance, Long> {
    List<Ordonnance> findByPatient(Patient patient);
    List<Ordonnance> findByMedecin(Medecin medecin);
}
