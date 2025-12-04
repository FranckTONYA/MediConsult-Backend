package org.projet.consultationmedicalebackend.services;

import org.projet.consultationmedicalebackend.models.Consentement;
import org.projet.consultationmedicalebackend.models.Medecin;
import org.projet.consultationmedicalebackend.models.Patient;
import org.projet.consultationmedicalebackend.models.StatutConsentement;
import org.projet.consultationmedicalebackend.repositories.ConsentementRepository;
import org.projet.consultationmedicalebackend.repositories.MedecinRepository;
import org.projet.consultationmedicalebackend.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ConsentementService {

    @Autowired
    private ConsentementRepository consentementRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private MedecinRepository medecinRepository;

    public Consentement save(Consentement consentement) {
        return consentementRepository.save(consentement);
    }

    public Consentement createConsent(Long patientId, Long medecinId) {
        Consentement consent = new Consentement();
        consent.setDate(LocalDate.now());
        consent.setStatut(StatutConsentement.EN_ATTENTE);

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient introuvable"));
        Medecin medecin = medecinRepository.findById(medecinId)
                .orElseThrow(() -> new RuntimeException("Médecin introuvable"));
        consent.setPatient(patient);
        consent.setMedecin(medecin);
        return consentementRepository.save(consent);
    }

    public List<Consentement> findAll() {
        return consentementRepository.findAll();
    }

    public Optional<Consentement> findById(Long id) {
        return consentementRepository.findById(id);
    }

    public List<Consentement> findByMedecin(Long medecinId) {
        return consentementRepository.findByMedecin_Id(medecinId);
    }

    public List<Consentement> findByPatient(Long patientId) {
        return consentementRepository.findByPatient_Id(patientId);
    }

    public Consentement updateStatus(Long id, String newStatus) {
        Consentement consentement = consentementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consentement introuvable"));
        consentement.setStatut(StatutConsentement.valueOf(newStatus));
        return consentementRepository.save(consentement);
    }

    public void delete(Long id) {
        consentementRepository.deleteById(id);
    }
}
