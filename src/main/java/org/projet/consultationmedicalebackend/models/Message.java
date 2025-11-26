package org.projet.consultationmedicalebackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "MESSAGE")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String contenu;

    @Column(nullable = false)
    private boolean lu = false;

    @Column(nullable = false)
    private LocalDateTime dateEnvoi;

    @Transient
    private Long emetteurId;

    @Transient
    private Long recepteurId;

    @JsonProperty("emetteurId")
    public Long getEmetteurId() {
        return emetteur != null ? emetteur.getId() : null;
    }

    @JsonProperty("recepteurId")
    public Long getRecepteurId() {
        return recepteur != null ? recepteur.getId() : null;
    }

    @ManyToOne
    @JoinColumn(name = "emetteur_id")
    @JsonIgnore
    private Utilisateur emetteur;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "recepteur_id")
    private Utilisateur recepteur;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "document_id")
    @JsonManagedReference
    private Document document;

    public Message() {
    }

    public Message(String contenu, boolean lu, LocalDateTime dateEnvoi, Utilisateur emetteur, Utilisateur recepteur, Document document) {
        this.contenu = contenu;
        this.lu = lu;
        this.dateEnvoi = dateEnvoi;
        this.emetteur = emetteur;
        this.recepteur = recepteur;
        this.document = document;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public boolean isLu() {
        return lu;
    }

    public void setLu(boolean lu) {
        this.lu = lu;
    }

    public LocalDateTime getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(LocalDateTime dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public void setEmetteurId(Long emetteurId) {
        this.emetteurId = emetteurId;
    }

    public void setRecepteurId(Long recepteurId) {
        this.recepteurId = recepteurId;
    }

    public Utilisateur getEmetteur() {
        return emetteur;
    }

    public void setEmetteur(Utilisateur emetteur) {
        this.emetteur = emetteur;
    }

    public Utilisateur getRecepteur() {
        return recepteur;
    }

    public void setRecepteur(Utilisateur recepteur) {
        this.recepteur = recepteur;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}
