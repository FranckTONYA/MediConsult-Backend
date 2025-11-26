package org.projet.consultationmedicalebackend.services.impl;

import org.projet.consultationmedicalebackend.models.Notification;
import org.projet.consultationmedicalebackend.models.Utilisateur;
import org.projet.consultationmedicalebackend.repositories.NotificationRepository;
import org.projet.consultationmedicalebackend.repositories.UtilisateurRepository;
import org.projet.consultationmedicalebackend.services.NotificationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UtilisateurRepository utilisateurRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository, UtilisateurRepository utilisateurRepository) {
        this.notificationRepository = notificationRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> findAll() {
        return notificationRepository.findAll();
    }

    @Override
    public Optional<Notification> findById(Long utilisateurId) {
        return notificationRepository.findById(utilisateurId);
    }

    @Override
    public List<Notification> findByUtilisateur(Long utilisateurId) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId).orElse(null);
        return notificationRepository.findByUtilisateur(utilisateur);
    }

    @Override
    public void marquerCommeLu(Long id) {
        notificationRepository.findById(id).ifPresent(n -> {
            n.setLu(true);
            notificationRepository.save(n);
        });
    }

    @Override
    public void delete(Long id) {
        notificationRepository.deleteById(id);
    }
}
