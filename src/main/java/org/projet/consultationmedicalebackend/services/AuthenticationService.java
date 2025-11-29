package org.projet.consultationmedicalebackend.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.apache.commons.text.StringEscapeUtils;
import org.projet.consultationmedicalebackend.models.*;
import org.projet.consultationmedicalebackend.repositories.*;
import org.projet.consultationmedicalebackend.security.CustomUserDetails;
import org.projet.consultationmedicalebackend.security.jwt.JwtService;
import org.projet.consultationmedicalebackend.security.utils.AesUtil;
import org.projet.consultationmedicalebackend.utils.CustomResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class AuthenticationService {

    private final UtilisateurRepository utilisateurRepository;
    private final PatientRepository patientRepository;
    private final MedecinRepository medecinRepository;
    private final DossierMedicalRepository dossierMedicalRepository;
    private final AdministrateurRepository administrateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final AesUtil aesUtil;

    public AuthenticationService(UtilisateurRepository utilisateurRepository,
                                 PatientRepository patientRepository,
                                 MedecinRepository medecinRepository,
                                 DossierMedicalRepository dossierMedicalRepository,
                                 AdministrateurRepository administrateurRepository,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService, EmailService emailService, AesUtil aesUtil) {
        this.utilisateurRepository = utilisateurRepository;
        this.patientRepository = patientRepository;
        this.medecinRepository = medecinRepository;
        this.dossierMedicalRepository = dossierMedicalRepository;
        this.administrateurRepository = administrateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.aesUtil = aesUtil;
    }

    public CustomResponse registerPatient(Patient patient) {
        CustomResponse response = new CustomResponse();
        // vérifier si l'email existe déjà
        if (utilisateurRepository.findByEmail(patient.getEmail()).isPresent()){
            response.status = false;
            response.message = "Cet e-mail existe déjà";
            return response;
        }
        patient.setMotDePasse(passwordEncoder.encode(patient.getMotDePasse()));
        patient.setRole(RoleUtilisateur.PATIENT);

        // Créer le dossier médical associé au patient
        DossierMedical dossierMedical = new DossierMedical();
        patient.setDossierMedical(dossierMedical); // Synchronisation bidirectionnelle via le setter

        // Sauvegarder uniquement le patient (cascade va persister le dossier)
        Patient patientSaved = patientRepository.save(patient);

        CustomUserDetails userDetails = new CustomUserDetails(patientSaved);

        response.status = true;
        response.message = jwtService.generateToken(userDetails);

        return response;
    }

    public CustomResponse registerMedecin(Medecin medecin) {
        CustomResponse response = new CustomResponse();
        // vérifier si l'email existe déjà
        if (utilisateurRepository.findByEmail(medecin.getEmail()).isPresent()){
            response.status = false;
            response.message = "Cet e-mail existe déjà";
            return response;
        }
        medecin.setMotDePasse(passwordEncoder.encode(medecin.getMotDePasse()));
        medecin.setRole(RoleUtilisateur.MEDECIN);
        medecinRepository.save(medecin);
        CustomUserDetails userDetails = new CustomUserDetails(medecin);

        response.status = true;
        response.message = jwtService.generateToken(userDetails);

        return response;
    }

    public CustomResponse registerAdmin(Administrateur admin) {
        CustomResponse response = new CustomResponse();
        // vérifier si l'email existe déjà
        if (utilisateurRepository.findByEmail(admin.getEmail()).isPresent()){
            response.status = false;
            response.message = "Cet e-mail existe déjà";
            return response;
        }
        admin.setMotDePasse(passwordEncoder.encode(admin.getMotDePasse()));
        admin.setRole(RoleUtilisateur.ADMINISTRATEUR);
        administrateurRepository.save(admin);
        CustomUserDetails userDetails = new CustomUserDetails(admin);

        response.status = true;
        response.message = jwtService.generateToken(userDetails);

        return response;
    }

    public Optional<String> login(String email, String motDePasse) {
        Optional<Utilisateur> userOpt = utilisateurRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            Utilisateur user = userOpt.get();
            if (passwordEncoder.matches(motDePasse, user.getMotDePasse())) {
                // Crée un CustomUserDetails pour Spring Security
                CustomUserDetails userDetails = new CustomUserDetails(user);
                return Optional.of(jwtService.generateToken(userDetails));
            }
        }
        return Optional.empty();
    }

    // 1) Appel initial d'inscription (ne stocke rien côté serveur)
    public CustomResponse startRegistrationPatient(Patient patient) {
        CustomResponse response = new CustomResponse();
        // vérifier si l'email existe déjà
        if (utilisateurRepository.findByEmail(patient.getEmail()).isPresent()){
            response.status = false;
            response.message = "Cet e-mail existe déjà";
            return response;
        }
        // 1. Générer OTP (code numérique 6 chiffres)
        String code = generateCode(6);

        // 2. Chiffrer le mot de passe avec AES (pour ne pas laisser le mot de passe en clair dans le JWT)
        String encryptedPassword = aesUtil.encrypt(patient.getMotDePasse());


        // 3. Construire les claims du JWT (ne pas inclure d'informations sensibles non chiffrées)
        Map<String, Object> claims = new HashMap<>();
        claims.put("niss", patient.getNiss());
        claims.put("dateNaissance", patient.getDateNaissance());
        claims.put("nom", patient.getNom());
        claims.put("prenom", patient.getPrenom());
        claims.put("sexe", patient.getSexe());
        claims.put("adresse", patient.getAdresse());
        claims.put("telephone", patient.getTelephone());
        claims.put("email", patient.getEmail());
        claims.put("pwd", encryptedPassword); // mot de passe chiffré
        claims.put("role", RoleUtilisateur.PATIENT.name());
        claims.put("code", code);
        claims.put("ts", LocalDateTime.now().toString());

        // 4. Générer le JWT de vérification (court TTL configuré)
        String verificationJwt = jwtService.generateVerificationToken(claims);

        // 5. Envoyer code par email (seul le code est dans le mail)
        String subject = "Code de vérification inscription - Medi Consult App";
        String htmlContent = buildVerificationEmail(patient.getPrenom(), code);

        emailService.envoyerEmail(patient.getEmail(), subject, htmlContent);

        // 6. Retourner le JWT au frontend (frontend doit le garder temporairement)
        response.status = true;
        response.message = verificationJwt;
        return response;
    }

    // 2) Vérification du code : frontend envoie { token, code }
    public CustomResponse verifyCodeAndCreatePatient(String token, String code) {
        CustomResponse response = new CustomResponse();

        Jws<Claims> jws = jwtService.parseToken(token);
        if (jws == null){
            response.status = false;
            response.message = "Token incorrect ou expiré";
            return response;
        }

        Claims claims = jws.getBody();

        // comparer code
        String codeInToken = (String) claims.get("code");
        if (codeInToken == null || !codeInToken.equals(code)){
            response.status = false;
            response.message = "Code incorrect ou expiré";
            return response;
        }

        // récupérer infos
        String email = (String) claims.get("email");
        String nom = (String) claims.get("nom");
        String prenom = (String) claims.get("prenom");
        Sexe sexe = Sexe.valueOf((String) claims.get("sexe"));
        String encryptedPwd = (String) claims.get("pwd");
        String niss = (String) claims.get("niss");
        Date dateNaissance = new Date((Long) claims.get("dateNaissance"));
        String adresse = (String) claims.get("adresse");
        String telephone = (String) claims.get("telephone");

        // déchiffrer le mot de passe AES et hasher avec BCrypt avant de stocker
        String plainPassword = aesUtil.decrypt(encryptedPwd);
//        String bcrypted = passwordEncoder.encode(plainPassword);

        Patient patient = new Patient();
        patient.setNom(nom);
        patient.setPrenom(prenom);
        patient.setSexe(sexe);
        patient.setEmail(email);
        patient.setMotDePasse(plainPassword);
        patient.setNiss(niss);
        patient.setDateNaissance(dateNaissance);
        patient.setAdresse(adresse);
        patient.setTelephone(telephone);
        patient.setRole(RoleUtilisateur.PATIENT);

        return registerPatient(patient);
    }

    public CustomResponse startPasswordReset(String email) {
        CustomResponse response = new CustomResponse();

        Optional<Utilisateur> opt = utilisateurRepository.findByEmail(email);
        if (opt.isEmpty()){
            response.status = false;
            response.message = "E-mail incorrect";
            return response;
        }

        String code = generateCode(6);
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("code", code);
        String token = jwtService.generateVerificationToken(claims);

        String subject = "Code de réinitialisation de mot de passe - Medi Consult App";
        String htmlContent = buildVerificationEmail(opt.get().getPrenom(), code);

        emailService.envoyerEmail(email, subject, htmlContent);

        response.status = true;
        response.message = token;
        return response;
    }

    public CustomResponse verifyCodeAndChangePassword(String token, String code, String newPassword) {
        CustomResponse response = new CustomResponse();

        Jws<Claims> jws = jwtService.parseToken(token);
        if (jws == null) {
            response.status = false;
            response.message = "Token incorrect ou expiré";
            return response;
        }

        String codeInToken = (String) jws.getBody().get("code");
        String email = (String) jws.getBody().get("email");
        if (!codeInToken.equals(code)) {
            response.status = false;
            response.message = "Code incorrect ou expiré";
            return response;
        }

        Optional<Utilisateur> opt = utilisateurRepository.findByEmail(email);
        if (opt.isEmpty()) {
            response.status = false;
            response.message = "E-mail incorrect";
            return response;
        }

        Utilisateur user = opt.get();
        user.setMotDePasse(passwordEncoder.encode(newPassword));
        utilisateurRepository.save(user);

        response.status = true;
        response.message = "Mot de passe reinitialisé";
        return response;
    }

    private String generateCode(int length) {
        Random rnd = new Random();
        int max = (int) Math.pow(10, length) - 1;
        int min = (int) Math.pow(10, length - 1);
        int number = rnd.nextInt(max - min + 1) + min;
        return String.valueOf(number);
    }

    private String buildVerificationEmail(String prenom, String code) {

        String safePrenom = StringEscapeUtils.escapeHtml4(prenom);
        String safeCode = StringEscapeUtils.escapeHtml4(code);

        StringBuilder sb = new StringBuilder();

        sb.append("<!DOCTYPE html><html lang=\"fr\"><head><meta charset=\"UTF-8\">")
                .append("<title>Code Vérification</title></head><body>");

        sb.append("<div style=\"font-family:Arial,sans-serif;background:#f5f7fa;padding:20px\">");

        sb.append("<div style=\"max-width:520px;margin:auto;background:#ffffff;")
                .append("padding:30px;border-radius:10px;\">");

        sb.append("<h2 style=\"text-align:center;color:#2563eb\">🔐 Vérification Medi Consult</h2>");

        sb.append("<p>Bonjour ").append(safePrenom).append(",</p>");
        sb.append("<p>Voici votre code de vérification :</p>");

        sb.append("<div style=\"background:#2563eb;color:white;padding:12px;")
                .append("border-radius:8px;text-align:center;font-size:24px;font-weight:bold;")
                .append("letter-spacing:4px;\">")
                .append(safeCode)
                .append("</div>");

        sb.append("<p>Ce code expirera dans quelques minutes.<br>")
                .append("Si vous n'êtes pas à l'origine de cette demande, ignorez ce message.</p>");

        sb.append("<p style=\"text-align:center;font-size:14px;color:#64748b\">")
                .append("Merci d'utiliser Medi Consult.</p>");

        sb.append("</div></div></body></html>");

        return sb.toString();
    }

}
