package org.projet.consultationmedicalebackend.services.impl;

import org.projet.consultationmedicalebackend.models.Message;
import org.projet.consultationmedicalebackend.models.Utilisateur;
import org.projet.consultationmedicalebackend.repositories.MessageRepository;
import org.projet.consultationmedicalebackend.repositories.UtilisateurRepository;
import org.projet.consultationmedicalebackend.services.MessageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UtilisateurRepository utilisateurRepository;

    public MessageServiceImpl(MessageRepository messageRepository, UtilisateurRepository utilisateurRepository) {
        this.messageRepository = messageRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public Message save(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public Optional<Message> findById(Long id) {
        return messageRepository.findById(id);
    }

    @Override
    public List<Message> findByExpediteur(Long expediteurId) {
        Utilisateur expediteur = utilisateurRepository.findById(expediteurId).orElse(null);
        return messageRepository.findByEmetteur(expediteur);
    }

    @Override
    public List<Message> findByDestinataire(Long destinataireId) {
        Utilisateur destinataire = utilisateurRepository.findById(destinataireId).orElse(null);
        return messageRepository.findByRecepteur(destinataire);
    }

    @Override
    public List<Message> findConversation(Long expediteurId, Long destinataireId) {
        Utilisateur expediteur = utilisateurRepository.findById(expediteurId).orElse(null);
        Utilisateur destinataire = utilisateurRepository.findById(destinataireId).orElse(null);
        return messageRepository.findConversation(expediteur, destinataire);
    }

    @Override
    public void delete(Long id) {
        messageRepository.deleteById(id);
    }
}
