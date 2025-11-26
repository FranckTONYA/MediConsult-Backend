package org.projet.consultationmedicalebackend.services;

import org.projet.consultationmedicalebackend.models.Message;

import java.util.List;
import java.util.Optional;

public interface MessageService {
    Message save(Message message);
    List<Message> findAll();
    Optional<Message> findById(Long id);
    List<Message> findByExpediteur(Long expediteurId);
    List<Message> findByDestinataire(Long destinataireId);
    List<Message> findConversation(Long expediteurId, Long destinataireId);
    void delete(Long id);
}
