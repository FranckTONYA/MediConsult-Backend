package org.projet.consultationmedicalebackend.repositories;

import org.projet.consultationmedicalebackend.models.PlanningMedecin;
import org.projet.consultationmedicalebackend.models.StatutPlanning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlanningMedecinRepository extends JpaRepository<PlanningMedecin, Long> {

    List<PlanningMedecin> findPlanningMedecinsByMedecin_Id(Long medecinId);

    @Query("SELECT p FROM PlanningMedecin p " +
            "WHERE p.medecin.id = :medecinId " +
            "AND p.statut = :statut " +
            "AND p.startDate <= :debut " +
            "AND p.endDate >= :fin")
    Optional<PlanningMedecin> findSlotContaining(
            @Param("medecinId") Long medecinId,
            @Param("debut") LocalDateTime debut,
            @Param("fin") LocalDateTime fin,
            @Param("statut") StatutPlanning statut);

}
