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

    @ManyToMany
    @JoinTable(
            name = "medecin_patient", // nom de la table de jointure
            joinColumns = @JoinColumn(name = "medecin_id"),  // clé étrangère vers Medecin
            inverseJoinColumns = @JoinColumn(name = "patient_id") // clé étrangère vers Patient
    )
//    @JsonManagedReference(value = "medecin-patients")
    @JsonIgnore
    private List<Patient> patients = new ArrayList<>();

    public Medecin() {
        super();
    }

    public Medecin(String nom, String prenom, String adresse, String telephone, String email, String motDePasse, String specialite, String numINAMI, List<Consultation> consultations, List<PlanningMedecin> plannings, List<Patient> patients) {
        super(nom, prenom, adresse, telephone, email, motDePasse, RoleUtilisateur.MEDECIN);
        this.specialite = specialite;
        this.numINAMI = numINAMI;
        this.consultations = consultations;
        this.plannings = plannings;
        this.patients = patients;
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

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    // Méthodes utilitaires
    public void addPatient(Patient patient) {
        patients.add(patient);
        patient.getMedecins().add(this);
    }

    public void removePatient(Patient patient) {
        patients.remove(patient);
        patient.getMedecins().remove(this);
    }
}
