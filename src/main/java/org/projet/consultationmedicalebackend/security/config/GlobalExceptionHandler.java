package org.projet.consultationmedicalebackend.security.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(org.springframework.web.multipart.MultipartException.class)
    public ResponseEntity<?> handleMultipartException(Exception ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof MaxUploadSizeExceededException) {
            return ResponseEntity
                    .status(413)
                    .body(Map.of(
                            "error", "Fichier trop volumineux",
                            "details", "La taille maximale autorisée est dépassée"
                    ));
        }
        return ResponseEntity
                .status(400)
                .body(Map.of("error", "Erreur de chargement du fichier"));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxSize(MaxUploadSizeExceededException ex) {
        return ResponseEntity
                .status(413)
                .body(Map.of(
                        "error", "Fichier trop volumineux",
                        "details", "La taille maximale autorisée est dépassée"
                ));
    }
}
