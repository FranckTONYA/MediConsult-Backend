package org.projet.consultationmedicalebackend.controllers;

import org.projet.consultationmedicalebackend.models.PlanningMedecin;
import org.projet.consultationmedicalebackend.services.PlanningMedecinService;
import org.projet.consultationmedicalebackend.utils.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/calendrier")
@CrossOrigin(origins = "*")
public class PlanningMedecinController {
    @Autowired
    private PlanningMedecinService planningMedecinService;

    @GetMapping("/get-by-medecin/{id}")
    public List<PlanningMedecin> getByMedecin(@PathVariable Long id) {
        return planningMedecinService.getByMedecin(id);
    }

    @PostMapping("/add-planning/{medecinId}")
    public ResponseEntity<?> add(@RequestBody PlanningMedecin planning,
                                 @PathVariable Long medecinId) {
        CustomResponse response = planningMedecinService.addPlanning(planning, medecinId);
        if (response.status)
            return ResponseEntity.ok(Map.of("message", response.message));
        else
            return ResponseEntity.badRequest().body(Map.of("error", response.message));
    }

    @DeleteMapping("/delete-by-id/{id}")
    public void delete(@PathVariable Long id) {
        planningMedecinService.delete(id);
    }
}
