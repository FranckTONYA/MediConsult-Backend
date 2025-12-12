package org.projet.consultationmedicalebackend.models;

public class DoctorRegistrationRequestDTO {
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String specialite;
    private String presentation;

    public DoctorRegistrationRequestDTO() {
    }

    public DoctorRegistrationRequestDTO(String nom, String prenom, String email, String telephone, String specialite, String presentation) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.specialite = specialite;
        this.presentation = presentation;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public String getPresentation() {
        return presentation;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }
}
