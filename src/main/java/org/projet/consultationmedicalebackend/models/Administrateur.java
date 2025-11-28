package org.projet.consultationmedicalebackend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "ADMINISTRATEUR")
public class Administrateur extends Utilisateur {
    @Enumerated(EnumType.STRING)
    private NiveauAcces niveau;

    public Administrateur() {
        super();
    }

    public Administrateur(String nom, String prenom, Sexe sexe, String adresse, String telephone, String email, String motDePasse, NiveauAcces niveau) {
        super(nom, prenom, sexe, adresse, telephone, email, motDePasse, RoleUtilisateur.ADMINISTRATEUR);
        this.niveau = niveau;
    }

    public NiveauAcces getNiveau() {
        return niveau;
    }

    public void setNiveau(NiveauAcces niveau) {
        this.niveau = niveau;
    }

}
