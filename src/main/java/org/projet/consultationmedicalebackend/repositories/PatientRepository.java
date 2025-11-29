package org.projet.consultationmedicalebackend.repositories;

import org.projet.consultationmedicalebackend.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByEmail(String email);
    Optional<Patient> findByNiss(String niss);
    Optional<Patient> findByDossierMedical_Id(Long dossierMedicalId);
}
