package org.projet.consultationmedicalebackend.repositories;

import org.projet.consultationmedicalebackend.models.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DossierMedicalRepository extends JpaRepository<DossierMedical, Long> {

    @Query("SELECT p.dossierMedical FROM Patient p " +
            "WHERE p.id = :patientId")
    Optional<DossierMedical> findByPatient(Long patientId);

    @Query("SELECT dm FROM DossierMedical dm " +
            "JOIN dm.patient p " +
            "JOIN p.medecins m " +
            "WHERE m.id = :medecinId")
    List<DossierMedical> findbyMedecin(@Param("medecinId") Long medecinId);
}
