package org.projet.consultationmedicalebackend.services.impl;

import jakarta.transaction.Transactional;
import org.projet.consultationmedicalebackend.models.*;
import org.projet.consultationmedicalebackend.repositories.ConsultationRepository;
import org.projet.consultationmedicalebackend.repositories.MedecinRepository;
import org.projet.consultationmedicalebackend.repositories.PatientRepository;
import org.projet.consultationmedicalebackend.repositories.PlanningMedecinRepository;
import org.projet.consultationmedicalebackend.services.ConsultationService;
import org.projet.consultationmedicalebackend.services.EmailService;
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
    private final EmailService emailService;

    public ConsultationServiceImpl(ConsultationRepository consultationRepository,
                                   PatientRepository patientRepository, MedecinRepository medecinRepository,
                                   PlanningMedecinRepository planningMedecinRepo, EmailService emailService) {
        this.consultationRepository = consultationRepository;
        this.patientRepository = patientRepository;
        this.medecinRepository = medecinRepository;
        this.planningMedecinRepo = planningMedecinRepo;
        this.emailService = emailService;
    }

    @Override
    public Consultation save(Consultation consultation) {
        consultation = consultationRepository.save(consultation);
        // Envoi de mail au patient
        String htmlContent1 = buildConsultationEmail(consultation,
                "Mise à jour de votre consultation", RoleUtilisateur.PATIENT);
        emailService.envoyerEmail(consultation.getPatient().getEmail(),
                "Mise à jour de votre consultation - MediConsult",
                htmlContent1);

        // Envoi de mail au médecin
        String htmlContent2 = buildConsultationEmail(consultation,
                "Mise à jour de votre consultation", RoleUtilisateur.MEDECIN);
        emailService.envoyerEmail(consultation.getMedecin().getEmail(),
                "Mise à jour de votre consultation - MediConsult",
                htmlContent2);

        return consultation;
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

        // Envoi de mail au patient
        String htmlContent1 = buildConsultationEmail(consultation,
                "Nouvelle consultation", RoleUtilisateur.PATIENT);
        emailService.envoyerEmail(consultation.getPatient().getEmail(),
                "Nouvelle consultation - MediConsult",
                htmlContent1);

        // Envoi de mail au médecin
        String htmlContent2 = buildConsultationEmail(consultation,
                "Nouvelle consultation", RoleUtilisateur.MEDECIN);
        emailService.envoyerEmail(consultation.getMedecin().getEmail(),
                "Nouvelle consultation - MediConsult",
                htmlContent2);

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
        if (statut == StatutRDV.REFUSER ){
            consultation.setStatut(StatutRDV.REFUSER);
            response.message = "Consultation refusée et créneau libéré";
        }

        else{
            consultation.setStatut(StatutRDV.ANNULER);
            response.message = "Consultation annulée et créneau libéré";
        }


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

        // Envoi de mail au patient
        String htmlContent1 = buildConsultationEmail(consultation,
                "Mise à jour de votre consultation", RoleUtilisateur.PATIENT);
        emailService.envoyerEmail(
                consultation.getPatient().getEmail(),
                "Mise à jour de votre consultation - MediConsult",
                htmlContent1
        );

        // Envoi de mail au médecin
        String htmlContent2 = buildConsultationEmail(consultation,
                "Mise à jour de votre consultation", RoleUtilisateur.MEDECIN);
        emailService.envoyerEmail(
                consultation.getMedecin().getEmail(),
                "Mise à jour de votre consultation - MediConsult",
                htmlContent2
        );

        response.status = true;
        return response;
    }

    private String buildConsultationEmail(Consultation consultation, String header, RoleUtilisateur role) {
        Patient patient = patientRepository.findById(consultation.getPatient().getId()).orElse(null);
        Medecin medecin = medecinRepository.findById(consultation.getMedecin().getId()).orElse(null);
        consultation.setPatient(patient);
        consultation.setMedecin(medecin);

        String prenomDestinataire;
        String lienVisioHtml = "";

        // Lien Visio seulement si statut CONFIRMER
        if (consultation.getStatut() == StatutRDV.CONFIRMER && consultation.getLienReunion() != null) {
            lienVisioHtml = "<p><b>Lien Visio :</b> <a href=\"" + consultation.getLienReunion()
                    + "\" style=\"color:#0d6efd;\">Rejoindre la consultation</a></p>";
        }

        if (role == RoleUtilisateur.PATIENT) {
            prenomDestinataire = patient.getPrenom();
        } else if (role == RoleUtilisateur.MEDECIN) {
            prenomDestinataire = medecin.getPrenom();
        } else {
            prenomDestinataire = "Utilisateur";
        }

        String infosHtml = role == RoleUtilisateur.PATIENT
                ? "<b>Médecin : </b>" + medecin.getNom() + " " + medecin.getPrenom()
                : "<b>Patient : </b>" + patient.getNom() + " " + patient.getPrenom();


        String dateDebut = consultation.getDebut().toString().replace("T", " ");
        String dateFin = consultation.getFin().toString().replace("T", " ");
        String statutTexte = getStatutTexte(consultation.getStatut());

        // Compte rendu si disponible
        String compteRenduHtml = "";
        if (consultation.getCompteRendu() != null && !consultation.getCompteRendu().isEmpty()) {
            compteRenduHtml = "<p><b>Compte rendu :</b> " + consultation.getCompteRendu() + "</p>";
        }

        return """
        <!DOCTYPE html>
        <html lang="fr">
        <head>
            <meta charset="UTF-8">
            <title>Notification Consultation</title>
        </head>
        <body style="margin:0;padding:0;font-family:Arial,sans-serif;background:#f5f7fa;">
            <div style="max-width:600px;margin:auto;background:white;border-radius:12px;overflow:hidden;box-shadow:0 4px 12px rgba(0,0,0,0.1)">
                
                <!-- Header -->
                <div style="background:#0d6efd;color:white;padding:20px;text-align:center;">
                    <h1 style="margin:0;font-size:24px;">%s</h1>
                </div>
                
                <!-- Body -->
                <div style="padding:20px;color:#333;font-size:16px;">
                    <p>Bonjour %s,</p>
                    <p>La demande de consultation est <b>%s</b>.</p>
                    <br/>
                    <h3 style="color:#0d6efd;">Détails de la consultation</h3>
                    <p>%s</p>
                    <p><b>Date & Heure :</b> %s - %s</p>
                    
                    %s
                    %s
                </div>
                
                <!-- Footer -->
                <div style="background:#f0f0f0;color:#64748b;padding:15px;text-align:center;font-size:14px;">
                    <p>Ceci est un email automatique de MediConsult. Merci de ne pas répondre à ce message.</p>
                    <p>Vous pouvez consulter la plateforme pour plus de détails.</p>
                </div>
            </div>
        </body>
        </html>
        """.formatted(
                header,
                prenomDestinataire,
                statutTexte,
                infosHtml,
                dateDebut,
                dateFin,
                lienVisioHtml,
                compteRenduHtml
        );
    }


    private String getStatutTexte(StatutRDV statut) {
        return switch (statut) {
            case EN_ATTENTE -> "en attente";
            case CONFIRMER -> "confirmée";
            case REFUSER -> "refusée";
            case ANNULER -> "annulée";
            case TERMINER -> "terminée";
            default -> "inconnue";
        };
    }

}
