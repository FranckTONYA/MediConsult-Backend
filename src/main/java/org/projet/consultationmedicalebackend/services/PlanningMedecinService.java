package org.projet.consultationmedicalebackend.services;

import org.projet.consultationmedicalebackend.models.Medecin;
import org.projet.consultationmedicalebackend.models.Patient;
import org.projet.consultationmedicalebackend.models.PlanningMedecin;
import org.projet.consultationmedicalebackend.repositories.MedecinRepository;
import org.projet.consultationmedicalebackend.repositories.PlanningMedecinRepository;
import org.projet.consultationmedicalebackend.utils.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlanningMedecinService {

    @Autowired
    private PlanningMedecinRepository planningMedecinRepo;

    @Autowired
    private MedecinRepository medecinRepository;

    public List<PlanningMedecin> getByMedecin(Long medecinId) {
        return planningMedecinRepo.findPlanningMedecinsByMedecin_Id(medecinId);
    }

    public CustomResponse addPlanning(PlanningMedecin planningMedecin, Long medecinId) {
        CustomResponse customResponse = new CustomResponse();
        Optional<Medecin> medecinOpt = medecinRepository.findById(medecinId);
        if (medecinOpt.isEmpty()) {
            customResponse.status = false;
            customResponse.message = "Médecin non trouvé";
            return customResponse;
        }

        Medecin medecin = medecinOpt.get();

        planningMedecin.setMedecin(medecin);
        planningMedecinRepo.save(planningMedecin);

        customResponse.status = true;
        customResponse.message = "Planning ajouté avec succès";
        return customResponse;
    }

    public void delete(Long id) {
        planningMedecinRepo.deleteById(id);
    }
}
