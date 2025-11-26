package org.projet.consultationmedicalebackend.services.impl;

import org.projet.consultationmedicalebackend.models.Paiement;
import org.projet.consultationmedicalebackend.models.Patient;
import org.projet.consultationmedicalebackend.repositories.PaiementRepository;
import org.projet.consultationmedicalebackend.repositories.PatientRepository;
import org.projet.consultationmedicalebackend.services.PaiementService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaiementServiceImpl implements PaiementService {

    private final PaiementRepository paiementRepository;
    private final PatientRepository patientRepository;

    public PaiementServiceImpl(PaiementRepository paiementRepository, PatientRepository patientRepository) {
        this.paiementRepository = paiementRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public Paiement save(Paiement paiement) {
        return paiementRepository.save(paiement);
    }

    @Override
    public List<Paiement> findAll() {
        return paiementRepository.findAll();
    }

    @Override
    public Optional<Paiement> findById(Long id) {
        return paiementRepository.findById(id);
    }

    @Override
    public List<Paiement> findByPatient(Long patientId) {
        Patient patient = patientRepository.findById(patientId).orElse(null);
        return paiementRepository.findByPatient(patient);
    }

    @Override
    public void delete(Long id) {
        paiementRepository.deleteById(id);
    }
}
