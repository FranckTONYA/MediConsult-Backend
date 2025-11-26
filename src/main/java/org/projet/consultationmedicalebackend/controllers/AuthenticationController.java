package org.projet.consultationmedicalebackend.controllers;


import org.projet.consultationmedicalebackend.models.Administrateur;
import org.projet.consultationmedicalebackend.models.Medecin;
import org.projet.consultationmedicalebackend.models.Patient;
import org.projet.consultationmedicalebackend.models.RoleUtilisateur;
import org.projet.consultationmedicalebackend.services.AuthenticationService;
import org.projet.consultationmedicalebackend.utils.CustomResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    // Démarrer l'inscription patient : retourne le token JWT de vérification (frontend le garde)
    @PostMapping("/register/patient-start")
    public ResponseEntity<?> startRegistration(@RequestBody Patient patient) {
        CustomResponse response = authenticationService.startRegistrationPatient(patient);
        if (response.status)
            return ResponseEntity.ok(Map.of("verificationToken", response.message));
        else
            return ResponseEntity.badRequest().body(Map.of("error", response.message));
    }

    // Vérifier le code d'inscription patient: frontend envoie { token, code }
    @PostMapping("/register/patient-verify-code")
    public ResponseEntity<?> verifyCode(@RequestBody Map<String, String> payload) {
        String registrationToken = payload.get("token");
        String code = payload.get("code");

        CustomResponse response = authenticationService.verifyCodeAndCreatePatient(registrationToken, code);

        if (response.status)
            return ResponseEntity.ok(Map.of("token", response.message, "role", RoleUtilisateur.PATIENT.name()));
        else
            return ResponseEntity.badRequest().body(Map.of("error", response.message));
    }

    @PostMapping("/register/patient")
    public ResponseEntity<?> registerPatient(@RequestBody Patient patient) {
        CustomResponse response = authenticationService.registerPatient(patient);
        if (response.status)
            return ResponseEntity.ok(Map.of("token", response.message, "role", RoleUtilisateur.PATIENT.name()));
        else
            return ResponseEntity.badRequest().body(Map.of("error", response.message));
    }

    @PostMapping("/register/medecin")
    public ResponseEntity<?> registerMedecin(@RequestBody Medecin medecin) {
        CustomResponse response = authenticationService.registerMedecin(medecin);
        if (response.status)
            return ResponseEntity.ok(Map.of("token", response.message, "role",  RoleUtilisateur.MEDECIN.name()));
        else
            return ResponseEntity.badRequest().body(Map.of("error", response.message));
    }

    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody Administrateur administrateur) {
        CustomResponse response = authenticationService.registerAdmin(administrateur);
        if (response.status)
            return ResponseEntity.ok(Map.of("token", response.message, "role",  RoleUtilisateur.ADMINISTRATEUR.name()));
        else
            return ResponseEntity.badRequest().body(Map.of("error", response.message));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String motDePasse = request.get("motDePasse");

        Optional<String> token = authenticationService.login(email, motDePasse);
        if (token.isPresent()) {
            return ResponseEntity.ok(Map.of("token", token.get()));
        } else {
            return ResponseEntity.status(401).body(Map.of("error", "Email ou mot de passe incorrect"));
        }
    }

    @PostMapping("/password/reset")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        CustomResponse response = authenticationService.startPasswordReset(email);
        if (response.status)
            return ResponseEntity.ok(Map.of("resetToken", response.message));
        else
            return ResponseEntity.badRequest().body(Map.of("error", response.message));
    }

    @PostMapping("/password/reset-verify-code")
    public ResponseEntity<?> verifyPasswordCode(@RequestBody Map<String, String> payload) {
        String token = payload.get("token");
        String code = payload.get("code");
        String newPassword = payload.get("newPassword");

        CustomResponse response = authenticationService.verifyCodeAndChangePassword(token, code, newPassword);
        if (response.status)
            return ResponseEntity.ok(Map.of("message", response.message));
        else
            return ResponseEntity.badRequest().body(Map.of("error", response.message));
    }
}
