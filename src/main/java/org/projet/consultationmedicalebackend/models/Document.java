package org.projet.consultationmedicalebackend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "DOCUMENT")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Enumerated(EnumType.STRING)
    private TypeDoc type;

    @Column(nullable = false)
    private String urlStockage;

    @ManyToOne
    @JoinColumn(name = "consultation_id")
    @JsonBackReference(value = "consultation-documents")
    private Consultation consultation;

    @ManyToOne
    @JoinColumn(name = "ordonnance_id")
    @JsonBackReference(value = "ordonnance-documents")
    private Ordonnance ordonnance;

    @OneToOne(mappedBy = "document") // côté inverse
    @JsonBackReference
    private Message message;

    @ManyToOne
    @JoinColumn(name = "dossierMedical_id")
    @JsonBackReference(value = "dossierMedical-documents")
    private DossierMedical dossierMedical;

    public Document() {
    }

    public Document(String nom, TypeDoc type, String urlStockage, Consultation consultation) {
        this.nom = nom;
        this.type = type;
        this.urlStockage = urlStockage;
        this.consultation = consultation;
    }

    public Document(String nom, TypeDoc type, String urlStockage, Ordonnance ordonnance) {
        this.nom = nom;
        this.type = type;
        this.urlStockage = urlStockage;
        this.ordonnance = ordonnance;
    }

    public Document(String nom, TypeDoc type, String urlStockage, Message message) {
        this.nom = nom;
        this.type = type;
        this.urlStockage = urlStockage;
        this.message = message;
    }

    public Document(String nom, TypeDoc type, String urlStockage, DossierMedical dossierMedical) {
        this.nom = nom;
        this.type = type;
        this.urlStockage = urlStockage;
        this.dossierMedical = dossierMedical;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public TypeDoc getType() {
        return type;
    }

    public void setType(TypeDoc type) {
        this.type = type;
    }

    public String getUrlStockage() {
        return urlStockage;
    }

    public void setUrlStockage(String urlStockage) {
        this.urlStockage = urlStockage;
    }

    public Consultation getConsultation() {
        return consultation;
    }

    public void setConsultation(Consultation consultation) {
        this.consultation = consultation;
    }

    public Ordonnance getOrdonnance() {
        return ordonnance;
    }

    public void setOrdonnance(Ordonnance ordonnance) {
        this.ordonnance = ordonnance;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public DossierMedical getDossierMedical() {
        return dossierMedical;
    }

    public void setDossierMedical(DossierMedical dossierMedical) {
        this.dossierMedical = dossierMedical;
    }
}
