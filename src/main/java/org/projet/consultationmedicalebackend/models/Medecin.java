package org.projet.consultationmedicalebackend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MEDECIN")
public class Medecin extends Utilisateur {

    @Column(nullable = false)
    private String specialite;

    @Column(nullable = false, unique = true)
    private String numINAMI;

    @OneToMany(mappedBy = "medecin", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Consultation> consultations;

    @OneToMany(mappedBy = "medecin", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonManagedReference
    @JsonIgnore
    private List<PlanningMedecin> plannings = new ArrayList<>();

    @OneToMany(mappedBy = "medecin", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Ordonnance> ordonnances = new ArrayList<>();

    @OneToMany(mappedBy = "medecin", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<AnalyseLabo> analyseLabos = new ArrayList<>();

    @OneToMany(mappedBy = "medecin", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<PlanningMedecin> planningMedecins = new ArrayList<>();

    @OneToMany(mappedBy = "medecin", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Consentement> consentements = new ArrayList<>();

    public Medecin() {
        super();
    }

    public Medecin(String nom, String prenom, Sexe sexe, String adresse, String telephone, String email, String motDePasse, String specialite, String numINAMI, List<Consultation> consultations, List<PlanningMedecin> plannings, List<Patient> patients) {
        super(nom, prenom, sexe, adresse, telephone, email, motDePasse, RoleUtilisateur.MEDECIN);
        this.specialite = specialite;
        this.numINAMI = numINAMI;
        this.consultations = consultations;
        this.plannings = plannings;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public String getNumINAMI() {
        return numINAMI;
    }

    public void setNumINAMI(String numINAMI) {
        this.numINAMI = numINAMI;
    }

    public List<Consultation> getConsultations() {
        return consultations;
    }

    public void setConsultations(List<Consultation> consultations) {
        this.consultations = consultations;
    }

    public List<PlanningMedecin> getPlannings() {
        return plannings;
    }

    public void setPlannings(List<PlanningMedecin> plannings) {
        this.plannings = plannings;
    }

    public List<Ordonnance> getOrdonnances() {
        return ordonnances;
    }

    public void setOrdonnances(List<Ordonnance> ordonnances) {
        this.ordonnances = ordonnances;
    }

    public List<AnalyseLabo> getAnalyseLabos() {
        return analyseLabos;
    }

    public void setAnalyseLabos(List<AnalyseLabo> analyseLabos) {
        this.analyseLabos = analyseLabos;
    }

    public List<PlanningMedecin> getPlanningMedecins() {
        return planningMedecins;
    }

    public void setPlanningMedecins(List<PlanningMedecin> planningMedecins) {
        this.planningMedecins = planningMedecins;
    }

    public List<Consentement> getConsentements() {
        return consentements;
    }

    public void setConsentements(List<Consentement> consentements) {
        this.consentements = consentements;
    }
}
