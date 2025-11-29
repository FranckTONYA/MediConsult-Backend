package org.projet.consultationmedicalebackend.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    @Value("${BREVO_API_KEY}")
    private String apiKey; // Clé stockée en variable d'environnement

    @Value("${EMAIL_SENDER}")
    private String senderEmail; // Peut être défini dans application.properties
    @Value("${SENDER_NAME}")
    private String senderName;   // Peut être défini dans application.properties

    private final RestTemplate restTemplate = new RestTemplate();

    public void envoyerEmail(String to, String subject, String htmlContent) {
        String url = "https://api.brevo.com/v3/smtp/email";

        Map<String, Object> body = Map.of(
                "sender", Map.of("name", senderName, "email", senderEmail),
                "to", List.of(Map.of("email", to)),
                "subject", subject,
                "htmlContent", htmlContent
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("api-key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            System.out.println("Email envoyé avec succès à " + to + " | status: " + response.getStatusCode());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.err.println("Erreur lors de l'envoi de l'email : " + e.getResponseBodyAsString());
            e.printStackTrace();
        }
    }
}

