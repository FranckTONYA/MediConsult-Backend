package org.projet.consultationmedicalebackend.services;

import org.projet.consultationmedicalebackend.models.Utilisateur;
import org.projet.consultationmedicalebackend.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurService implements UserDetailsService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    /**
     * Méthode obligatoire de UserDetailsService.
     * Appelée automatiquement par Spring Security pour charger un utilisateur via son email.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'email : " + email));

        // Construction d’un objet UserDetails compatible Spring Security
        return User.builder()
                .username(utilisateur.getEmail())
                .password(utilisateur.getMotDePasse())
                .roles(utilisateur.getRole().name()) // PATIENT, MEDECIN, ADMINISTRATEUR
                .build();
    }
}
