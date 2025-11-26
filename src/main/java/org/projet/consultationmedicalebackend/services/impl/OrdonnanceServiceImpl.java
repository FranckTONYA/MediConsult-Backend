package org.projet.consultationmedicalebackend.services.impl;

import org.projet.consultationmedicalebackend.models.Medecin;
import org.projet.consultationmedicalebackend.models.Ordonnance;
import org.projet.consultationmedicalebackend.models.Patient;
import org.projet.consultationmedicalebackend.repositories.MedecinRepository;
import org.projet.consultationmedicalebackend.repositories.OrdonnanceRepository;
import org.projet.consultationmedicalebackend.repositories.PatientRepository;
import org.projet.consultationmedicalebackend.services.OrdonnanceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrdonnanceServiceImpl implements OrdonnanceService {

    private final OrdonnanceRepository ordonnanceRepository;
    private final PatientRepository patientRepository;
    private final MedecinRepository medecinRepository;

    public OrdonnanceServiceImpl(OrdonnanceRepository ordonnanceRepository, PatientRepository patientRepository, MedecinRepository medecinRepository) {
        this.ordonnanceRepository = ordonnanceRepository;
        this.patientRepository = patientRepository;
        this.medecinRepository = medecinRepository;
    }

    @Override
    public Ordonnance save(Ordonnance ordonnance) {
        return ordonnanceRepository.save(ordonnance);
    }

    @Override
    public List<Ordonnance> findAll() {
        return ordonnanceRepository.findAll();
    }

    @Override
    public Optional<Ordonnance> findById(Long id) {
        return ordonnanceRepository.findById(id);
    }

    @Override
    public List<Ordonnance> findByPatient(Long patientId) {
        Patient patient = patientRepository.findById(patientId).orElse(null);
        return ordonnanceRepository.findByPatient(patient);
    }

    @Override
    public List<Ordonnance> findByMedecin(Long medecinId) {
        Medecin medecin = medecinRepository.findById(medecinId).orElse(null);
        return ordonnanceRepository.findByMedecin(medecin);
    }

    @Override
    public void delete(Long id) {
        ordonnanceRepository.deleteById(id);
    }
}
