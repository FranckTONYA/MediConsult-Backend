package org.projet.consultationmedicalebackend.services.impl;

import org.projet.consultationmedicalebackend.models.Administrateur;
import org.projet.consultationmedicalebackend.repositories.AdministrateurRepository;
import org.projet.consultationmedicalebackend.services.AdministrateurService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdministrateurServiceImpl implements AdministrateurService {

    private final AdministrateurRepository administrateurRepository;

    public AdministrateurServiceImpl(AdministrateurRepository administrateurRepository) {
        this.administrateurRepository = administrateurRepository;
    }

    @Override
    public Administrateur save(Administrateur administrateur) {
        return administrateurRepository.save(administrateur);
    }

    @Override
    public List<Administrateur> findAll() {
        return administrateurRepository.findAll();
    }

    @Override
    public Optional<Administrateur> findById(Long id) {
        return administrateurRepository.findById(id);
    }

    @Override
    public Optional<Administrateur> findByEmail(String email) {
        return administrateurRepository.findByEmail(email);
    }

    @Override
    public void delete(Long id) {
        administrateurRepository.deleteById(id);
    }
}
