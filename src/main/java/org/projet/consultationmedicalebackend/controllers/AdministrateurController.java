package org.projet.consultationmedicalebackend.controllers;

import org.projet.consultationmedicalebackend.models.Administrateur;
import org.projet.consultationmedicalebackend.services.AdministrateurService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/administrateur")
@CrossOrigin(origins = "*")
public class AdministrateurController {

    private final AdministrateurService administrateurService;

    public AdministrateurController(AdministrateurService administrateurService) {
        this.administrateurService = administrateurService;
    }

    @GetMapping("/getAll")
    public List<Administrateur> getAll() {
        return administrateurService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Administrateur> getById(@PathVariable Long id) {
        return administrateurService.findById(id);
    }

    @PostMapping
    public Administrateur create(@RequestBody Administrateur admin) {
        return administrateurService.save(admin);
    }

    @PutMapping("/update/{id}")
    public Administrateur update(@PathVariable Long id, @RequestBody Administrateur admin) {
        admin.setId(id);
        return administrateurService.save(admin);
    }

    @DeleteMapping("/delete-by-id/{id}")
    public void delete(@PathVariable Long id) {
        administrateurService.delete(id);
    }
}
