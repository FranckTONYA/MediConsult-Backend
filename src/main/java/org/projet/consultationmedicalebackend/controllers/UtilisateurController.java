package org.projet.consultationmedicalebackend.controllers;

import org.projet.consultationmedicalebackend.models.Utilisateur;
import org.projet.consultationmedicalebackend.repositories.UtilisateurRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/utilisateur")
@CrossOrigin(origins = "*")
public class UtilisateurController {

    private UtilisateurRepository utilisateurRepository;

    public UtilisateurController(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @GetMapping("/getAll")
    public List<Utilisateur> getAll() {
        return utilisateurRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Utilisateur> getById(@PathVariable Long id) {
        return utilisateurRepository.findById(id);
    }

    @GetMapping("/find-by-email/{email}")
    public Optional<Utilisateur> getByEmail(@PathVariable String email) {
        return utilisateurRepository.findByEmail(email);
    }

    @PostMapping
    public Utilisateur create(@RequestBody Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }

    @PutMapping("/update/{id}")
    public Utilisateur update(@PathVariable Long id, @RequestBody Utilisateur utilisateur) {
        utilisateur.setId(id);
        return utilisateurRepository.save(utilisateur);
    }

    @DeleteMapping("/delete-by-id/{id}")
    public void delete(@PathVariable Long id) {
        utilisateurRepository.deleteById(id);
    }
}
