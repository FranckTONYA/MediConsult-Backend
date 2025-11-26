package org.projet.consultationmedicalebackend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "DOSSIER_MEDICAL")
public class DossierMedical {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String groupeSanguin;

    @ElementCollection
    private List<String> allergies;

    @ElementCollection
    private List<String> vaccinations;

    @ElementCollection
    private List<String> antecedentsMedicaux;

    @ElementCollection
    private List<String> remarques;

    @OneToOne
    @JoinColumn(name = "patient_id")
    @JsonManagedReference
    private Patient patient;

    public DossierMedical() {
    }

    public DossierMedical(String groupeSanguin, List<String> allergies, List<String> vaccinations, List<String> antecedentsMedicaux, List<String> remarques, Patient patient) {
        this.groupeSanguin = groupeSanguin;
        this.allergies = allergies;
        this.vaccinations = vaccinations;
        this.antecedentsMedicaux = antecedentsMedicaux;
        this.remarques = remarques;
        this.patient = patient;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupeSanguin() {
        return groupeSanguin;
    }

    public void setGroupeSanguin(String groupeSanguin) {
        this.groupeSanguin = groupeSanguin;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }

    public List<String> getVaccinations() {
        return vaccinations;
    }

    public void setVaccinations(List<String> vaccinations) {
        this.vaccinations = vaccinations;
    }

    public List<String> getAntecedentsMedicaux() {
        return antecedentsMedicaux;
    }

    public void setAntecedentsMedicaux(List<String> antecedentsMedicaux) {
        this.antecedentsMedicaux = antecedentsMedicaux;
    }

    public List<String> getRemarques() {
        return remarques;
    }

    public void setRemarques(List<String> remarques) {
        this.remarques = remarques;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
