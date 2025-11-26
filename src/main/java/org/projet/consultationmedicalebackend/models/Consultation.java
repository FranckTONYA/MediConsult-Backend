package org.projet.consultationmedicalebackend.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "CONSULTATION")
public class Consultation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime debut;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fin;

    private String compteRendu;

    private String lienReunion;

    @Enumerated(EnumType.STRING)
    private StatutRDV statut;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "medecin_id")
    private Medecin medecin;

    @OneToMany(mappedBy = "consultation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "consultation-documents")
    private List<Document> documents;

    public Consultation() {
    }

    public Consultation(LocalDateTime debut, LocalDateTime fin, String compteRendu, String lienReunion, StatutRDV statut, Patient patient, Medecin medecin, List<Document> documents) {
        this.debut = debut;
        this.fin = fin;
        this.compteRendu = compteRendu;
        this.lienReunion = lienReunion;
        this.statut = statut;
        this.patient = patient;
        this.medecin = medecin;
        this.documents = documents;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDebut() {
        return debut;
    }

    public void setDebut(LocalDateTime debut) {
        this.debut = debut;
    }

    public LocalDateTime getFin() {
        return fin;
    }

    public void setFin(LocalDateTime fin) {
        this.fin = fin;
    }

    public String getCompteRendu() {
        return compteRendu;
    }

    public void setCompteRendu(String compteRendu) {
        this.compteRendu = compteRendu;
    }

    public String getLienReunion() {
        return lienReunion;
    }

    public void setLienReunion(String lienReunion) {
        this.lienReunion = lienReunion;
    }

    public StatutRDV getStatut() {
        return statut;
    }

    public void setStatut(StatutRDV statut) {
        this.statut = statut;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Medecin getMedecin() {
        return medecin;
    }

    public void setMedecin(Medecin medecin) {
        this.medecin = medecin;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }
}
