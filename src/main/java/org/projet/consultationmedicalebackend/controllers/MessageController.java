package org.projet.consultationmedicalebackend.controllers;

import org.projet.consultationmedicalebackend.models.*;
import org.projet.consultationmedicalebackend.repositories.UtilisateurRepository;
import org.projet.consultationmedicalebackend.services.DocumentService;
import org.projet.consultationmedicalebackend.services.FileStorageService;
import org.projet.consultationmedicalebackend.services.MessageService;
import org.projet.consultationmedicalebackend.services.UtilisateurService;
import org.projet.consultationmedicalebackend.utils.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/message")
@CrossOrigin(origins = "*")
public class MessageController {

    private final MessageService messageService;
    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/getAll")
    public List<Message> getAll() {
        return messageService.findAll();
    }

    @GetMapping("/find-by-id/{id}")
    public Optional<Message> getById(@PathVariable Long id) {
        return messageService.findById(id);
    }

    @GetMapping("/conversation/{user1Id}/{user2Id}")
    public List<Message> getConversation(@PathVariable Long user1Id, @PathVariable Long user2Id) {
        return messageService.findConversation(user1Id, user2Id);
    }

    @PostMapping
    public Message create(@RequestBody Message message) {
        return messageService.save(message);
    }

    @DeleteMapping("/delete-by-id/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Message> messsageOpt = messageService.findById(id);

        if (messsageOpt.isPresent()) {
            Message message = messsageOpt.get();

            // Suppression le fichier lié
            if (message.getDocument() != null) {
                // Supprime le fichier physique
                File f = new File(System.getProperty("user.dir") + "/uploads-messages/"
                        + message.getDocument().getUrlStockage());
                if (f.exists()) f.delete();

                // Supprime l'enregistrement en DB
                documentService.delete(message.getDocument().getId());
            }

            // Supprime le message
            messageService.delete(id);

            return ResponseEntity.ok(Map.of("message", "Message et son fichier supprimés avec succès"));
        } else {
            return ResponseEntity.status(404).body(Map.of("error", "Message introuvable"));
        }
    }

    @PostMapping(value = "/create-with-file", consumes = "multipart/form-data")
    public ResponseEntity<?> createWithFile(
            @RequestParam("contenu") String contenu,
            @RequestParam("emetteurId") Long emetteurId,
            @RequestParam("recepteurId") Long recepteurId,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        try {
                Utilisateur emetteur = utilisateurRepository.findById(emetteurId)
                        .orElseThrow(() -> new RuntimeException("Emetteur introuvable"));

                Utilisateur recepteur = utilisateurRepository.findById(recepteurId)
                        .orElseThrow(() -> new RuntimeException("Recepteur introuvable"));

            Message message = new Message();
            message.setContenu(contenu);
            message.setEmetteur(emetteur);
            message.setRecepteur(recepteur);
            message.setDateEnvoi(LocalDateTime.now());

            Message messageSaved = messageService.save(message);

            // Sauvegarde du fichier
            if (file != null && !file.isEmpty()){
                String folder = "/uploads-messages/";

                CustomResponse response = fileStorageService.saveFile(file, folder);

                if(!response.status)
                    return ResponseEntity.badRequest().body(Map.of("error", response.message));

                String generatedName = response.message;

                TypeDoc type = fileStorageService.detectFileType(file);

                Document doc = new Document(
                        generatedName,
                        type,
                        generatedName,
                        messageSaved
                );
                documentService.save(doc);

                messageSaved.setDocument(doc);
                messageService.save(messageSaved);
            }

            return ResponseEntity.ok(Map.of("message", "Message crée avec succès"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body((Map.of("error", "Erreur serveur :" + e.getMessage())));
        }
    }

    @PutMapping("/update/{id}")
    public Message update(@PathVariable Long id, @RequestBody Message message) {

        Message messageFound = messageService.findById(id)
                .orElseThrow(() -> new RuntimeException("Message non trouvée"));

        // Mise à jour
        messageFound.setContenu(message.getContenu());
        messageFound.setLu(message.isLu());

        return messageService.save(messageFound);
    }
}
