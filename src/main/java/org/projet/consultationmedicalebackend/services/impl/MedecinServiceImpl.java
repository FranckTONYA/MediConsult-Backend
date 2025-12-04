package org.projet.consultationmedicalebackend.services.impl;

import org.projet.consultationmedicalebackend.models.Medecin;
import org.projet.consultationmedicalebackend.models.Patient;
import org.projet.consultationmedicalebackend.repositories.MedecinRepository;
import org.projet.consultationmedicalebackend.repositories.PatientRepository;
import org.projet.consultationmedicalebackend.services.MedecinService;
import org.projet.consultationmedicalebackend.utils.CustomResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedecinServiceImpl implements MedecinService {

    private final MedecinRepository medecinRepository;
    private final PatientRepository patientRepository;

    public MedecinServiceImpl(MedecinRepository medecinRepository, PatientRepository patientRepository ) {
        this.medecinRepository = medecinRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public Medecin save(Medecin medecin) {
        return medecinRepository.save(medecin);
    }

    @Override
    public List<Medecin> findAll() {
        return medecinRepository.findAll();
    }

    @Override
    public Optional<Medecin> findById(Long id) {
        return medecinRepository.findById(id);
    }

    @Override
    public Optional<Medecin> findByInami(String inami) {
        return medecinRepository.findByNumINAMI(inami);
    }

    @Override
    public Optional<Medecin> findByEmail(String email) {
        return medecinRepository.findByEmail(email);
    }

    @Override
    public List<Medecin> findBySpecialite(String specialite) {
        return medecinRepository.findBySpecialite(specialite);
    }

    @Override
    public void delete(Long id) {
        medecinRepository.deleteById(id);
    }

}
