package org.projet.consultationmedicalebackend.services.impl;

import org.projet.consultationmedicalebackend.models.DossierMedical;
import org.projet.consultationmedicalebackend.models.Patient;
import org.projet.consultationmedicalebackend.repositories.DossierMedicalRepository;
import org.projet.consultationmedicalebackend.repositories.PatientRepository;
import org.projet.consultationmedicalebackend.services.PatientService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final DossierMedicalRepository dossierMedicalRepository;

    public PatientServiceImpl(PatientRepository patientRepository,
                              DossierMedicalRepository dossierMedicalRepository) {
        this.patientRepository = patientRepository;
        this.dossierMedicalRepository = dossierMedicalRepository;
    }

    @Override
    public Patient save(Patient patient) {
        if (patient.getDossierMedical() == null) {
            // Créer le dossier médical associé au patient
            DossierMedical dossierMedical = new DossierMedical();
//            dossierMedical.setPatient(patient);
//            dossierMedicalRepository.save(dossierMedical);

            // Associer le dossier medical au patient
            patient.setDossierMedical(dossierMedical);
        }

        return patientRepository.save(patient);
    }

    @Override
    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    @Override
    public Optional<Patient> findById(Long id) {
        return patientRepository.findById(id);
    }

    @Override
    public Optional<Patient> findByEmail(String email) {
        return patientRepository.findByEmail(email);
    }

    @Override
    public Optional<Patient> findByNiss(String niss) {
        return patientRepository.findByNiss(niss);
    }

    @Override
    public Optional<Patient> findByDossier(Long dossierId) {
        return patientRepository.findByDossierMedical_Id(dossierId);
    }

    @Override
    public void delete(Long id) {
        patientRepository.deleteById(id);
    }
}
