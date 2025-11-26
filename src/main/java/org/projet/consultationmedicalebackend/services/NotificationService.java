package org.projet.consultationmedicalebackend.services;

import org.projet.consultationmedicalebackend.models.Notification;
import java.util.List;
import java.util.Optional;

public interface NotificationService {
    Notification save(Notification notification);
    List<Notification> findAll();
    List<Notification> findByUtilisateur(Long utilisateurId);
    Optional<Notification> findById(Long utilisateurId);
    void marquerCommeLu(Long id);
    void delete(Long id);
}
