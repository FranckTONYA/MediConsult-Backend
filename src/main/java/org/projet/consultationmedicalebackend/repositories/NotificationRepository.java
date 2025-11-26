package org.projet.consultationmedicalebackend.repositories;

import org.projet.consultationmedicalebackend.models.Notification;
import org.projet.consultationmedicalebackend.models.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUtilisateur(Utilisateur utilisateur);
}
