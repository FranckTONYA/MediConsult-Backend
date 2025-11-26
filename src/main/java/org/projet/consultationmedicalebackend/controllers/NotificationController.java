package org.projet.consultationmedicalebackend.controllers;

import org.projet.consultationmedicalebackend.models.Notification;
import org.projet.consultationmedicalebackend.services.NotificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notification")
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/getAll")
    public List<Notification> getAll() {
        return notificationService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Notification> getById(@PathVariable Long id) {
        return notificationService.findById(id);
    }

    @GetMapping("/find-by-utilisateur/{idUtilisateur}")
    public List<Notification> getByUtilisateur(@PathVariable Long idUtilisateur) {
        return notificationService.findByUtilisateur(idUtilisateur);
    }

    @PostMapping
    public Notification create(@RequestBody Notification notification) {
        return notificationService.save(notification);
    }

    @PutMapping("/update/{id}")
    public Notification update(@PathVariable Long id, @RequestBody Notification notification) {
        notification.setId(id);
        return notificationService.save(notification);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        notificationService.delete(id);
    }
}
