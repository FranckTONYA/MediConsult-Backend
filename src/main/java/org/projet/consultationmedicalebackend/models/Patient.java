package org.projet.consultationmedicalebackend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "PATIENT")
public class Patient extends Utilisateur{
    @Column(nullable = false, unique = true)
    private String niss;

    @Column(nullable = false)
    private Date dateNaissance;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "dossier_id")
    @JsonBackReference
    private DossierMedical dossierMedical;

    @ManyToMany(mappedBy = "patients") // côté inverse de la relation
//    @JsonBackReference(value = "medecin-patients")
    @JsonIgnore
    private List<Medecin> medecins = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Consultation> consultations;

    public Patient() {
        super();
    }

    public Patient(String nom, String prenom, Sexe sexe, String adresse, String telephone, String email, String motDePasse, String niss, Date dateNaissance, DossierMedical dossierMedical, List<Medecin> medecins,  List<Consultation> consultations) {
        super(nom, prenom, sexe, adresse, telephone, email, motDePasse, RoleUtilisateur.PATIENT);
        this.niss = niss;
        this.dateNaissance = dateNaissance;
        this.dossierMedical = dossierMedical;
        this.medecins = medecins;
        this.consultations = consultations;
    }

    public String getNiss() {
        return niss;
    }

    public void setNiss(String niss) {
        this.niss = niss;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public DossierMedical getDossierMedical() {
        return dossierMedical;
    }

    public void setDossierMedical(DossierMedical dossierMedical) {
        this.dossierMedical = dossierMedical;
    }

    public List<Medecin> getMedecins() {
        return medecins;
    }

    public void setMedecins(List<Medecin> medecins) {
        this.medecins = medecins;
    }

    public List<Consultation> getConsultations() {
        return consultations;
    }

    public void setConsultations(List<Consultation> consultations) {
        this.consultations = consultations;
    }
}
