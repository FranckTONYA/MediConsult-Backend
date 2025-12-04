package org.projet.consultationmedicalebackend.services.impl;

import jakarta.transaction.Transactional;
import org.projet.consultationmedicalebackend.models.*;
import org.projet.consultationmedicalebackend.repositories.ConsultationRepository;
import org.projet.consultationmedicalebackend.repositories.MedecinRepository;
import org.projet.consultationmedicalebackend.repositories.PatientRepository;
import org.projet.consultationmedicalebackend.repositories.PlanningMedecinRepository;
import org.projet.consultationmedicalebackend.services.ConsultationService;
import org.projet.consultationmedicalebackend.utils.CustomResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConsultationServiceImpl implements ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final PatientRepository patientRepository;
    private final MedecinRepository medecinRepository;
    private final PlanningMedecinRepository planningMedecinRepo;

    public ConsultationServiceImpl(ConsultationRepository consultationRepository,
                                   PatientRepository patientRepository, MedecinRepository medecinRepository,
                                   PlanningMedecinRepository planningMedecinRepo) {
        this.consultationRepository = consultationRepository;
        this.patientRepository = patientRepository;
        this.medecinRepository = medecinRepository;
        this.planningMedecinRepo = planningMedecinRepo;
    }

    @Override
    public Consultation save(Consultation consultation) {
        return consultationRepository.save(consultation);
    }

    @Override
    public List<Consultation> findAll() {
        return consultationRepository.findAll();
    }

    @Override
    public Optional<Consultation> findById(Long id) {
        return consultationRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        consultationRepository.deleteById(id);
    }

    @Override
    public List<Consultation> findByPatient(Long patientId) {
        Patient patient = patientRepository.findById(patientId).orElse(null);
        return consultationRepository.findByPatient(patient);
    }

    @Override
    public List<Consultation> findByMedecin(Long medecinId) {
        Medecin medecin = medecinRepository.findById(medecinId).orElse(null);
        return consultationRepository.findByMedecin(medecin);
    }

    @Transactional
    @Override
    public CustomResponse createConsultation(Consultation consultation) {
        CustomResponse response = new CustomResponse();

        LocalDateTime debut = consultation.getDebut();
        LocalDateTime fin = consultation.getFin();
        Long medecinId = consultation.getMedecin().getId();

        Optional<PlanningMedecin> slotOpt = planningMedecinRepo.findSlotContaining(medecinId, debut, fin, StatutPlanning.DISPONIBLE);

        if (slotOpt.isEmpty()) {
            response.status = false;
            response.message = "Créneau non valide";
            return response;
        }

        PlanningMedecin slot = slotOpt.get();

        // Supprimer le créneau parent (on va le remplacer par des fragments)
        planningMedecinRepo.delete(slot);

        // Créneau avant
        if (slot.getStartDate().isBefore(debut)) {
            PlanningMedecin before = new PlanningMedecin();
            before.setMedecin(slot.getMedecin());
            before.setStartDate(slot.getStartDate());
            before.setEndDate(debut);
            before.setStatut(StatutPlanning.DISPONIBLE);
            planningMedecinRepo.save(before);
        }

        // Créneau après
        if (slot.getEndDate().isAfter(fin)) {
            PlanningMedecin after = new PlanningMedecin();
            after.setMedecin(slot.getMedecin());
            after.setStartDate(fin);
            after.setEndDate(slot.getEndDate());
            after.setStatut(StatutPlanning.DISPONIBLE);
            planningMedecinRepo.save(after);
        }

        // Rendre le créneau demandé indisponible
        PlanningMedecin reserver = new PlanningMedecin();
        reserver.setMedecin(slot.getMedecin());
        reserver.setStartDate(consultation.getDebut());
        reserver.setEndDate(consultation.getFin());
        reserver.setStatut(StatutPlanning.INDISPONIBLE);
        planningMedecinRepo.save(reserver);

        // Le Générer un lien Jitsi pour visio
        String roomKey = UUID.randomUUID().toString().replace("-", "");
        String jitsiLink = "https://meet.jit.si/" + roomKey;
        consultation.setLienReunion(jitsiLink);

        consultationRepository.save(consultation);

        response.status = true;
        response.message = "Créneau réservé avec succcé";
        return response;
    }

    @Override
    public CustomResponse cancelOrRefuseConsultation(Long id, StatutRDV statut) {
        CustomResponse response = new CustomResponse();
        Optional<Consultation> opt = consultationRepository.findById(id);
        if(opt.isEmpty()){
            response.status = false;
            response.message = "Consultation introuvable";
        }

        Consultation consultation = opt.get();


        // Mettre à jour le statut
        if (statut == StatutRDV.REFUSER )
            consultation.setStatut(StatutRDV.REFUSER);
        else
            consultation.setStatut(StatutRDV.ANNULER);

        consultationRepository.save(consultation);

        // Restaurer le créneau réservé à DISPONIBLE
        PlanningMedecin slot = planningMedecinRepo.findSlotContaining(
                consultation.getMedecin().getId(),
                consultation.getDebut(),
                consultation.getFin(),
                StatutPlanning.INDISPONIBLE
        ).orElse(null);

        if(slot != null){
            slot.setStatut(StatutPlanning.DISPONIBLE);
            planningMedecinRepo.save(slot);
        }

        response.status = true;
        response.message = "Consultation annulée et créneau libéré";
        return response;
    }

}
