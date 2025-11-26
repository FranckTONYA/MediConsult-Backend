package org.projet.consultationmedicalebackend.services;

import org.projet.consultationmedicalebackend.models.Document;
import org.projet.consultationmedicalebackend.models.TypeDoc;
import org.projet.consultationmedicalebackend.utils.CustomResponse;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FileStorageService {

    private final Path root = Paths.get("uploads");

    public FileStorageService() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Impossible de créer le dossier d’upload");
        }
    }

    public CustomResponse saveFile(MultipartFile file, String folder) throws Exception {

        CustomResponse response = new CustomResponse();

        // Vérification format
        if (!isSupportedFileType(file)) {
            response.status = false;
            response.message = "Format non supporté : " + file.getOriginalFilename();
            return response;
        }

        if (hasForbiddenExtension(file)){
            response.status = false;
            response.message = "Extension dangereuse interdite : " + file.getOriginalFilename();
            return response;
        }

        String uploadDir = System.getProperty("user.dir") + folder;

        Path path = Paths.get(uploadDir);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        String generatedName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path destination = path.resolve(generatedName);

        file.transferTo(destination.toFile());

        response.status = true;
        response.message = generatedName;
        return response;
    }

    public void deleteFile(String filePath) {
        try {
            if (filePath == null) return;

            Path realPath = root.resolve(filePath.replace("/uploads/", ""));
            Files.deleteIfExists(realPath);
        } catch (IOException e) {
            throw new RuntimeException("Erreur suppression fichier : " + e.getMessage());
        }
    }

    public TypeDoc detectFileType(MultipartFile file) {
        String mime = file.getContentType().toLowerCase();

        if (mime.startsWith("image/")) {
            return TypeDoc.IMAGE;
        }
        if (mime.equals("application/pdf")) {
            return TypeDoc.PDF;
        }

        return TypeDoc.TEXT;
    }

    private static final List<String> SUPPORTED_MIME_TYPES = List.of(
            "application/pdf",
            "text/plain",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "application/vnd.ms-powerpoint",
            "application/vnd.openxmlformats-officedocument.presentationml.presentation",
            "image/jpeg",
            "image/png",
            "image/jpg",
            "image/gif"
    );

    public boolean isSupportedFileType(MultipartFile file) {
        String mime = file.getContentType();
        return SUPPORTED_MIME_TYPES.contains(mime);
    }

    private static final List<String> FORBIDDEN_EXT = List.of("exe", "bat", "cmd", "sh", "js", "php");

    public boolean hasForbiddenExtension(MultipartFile file) {
        String name = file.getOriginalFilename().toLowerCase();
        return FORBIDDEN_EXT.stream().anyMatch(name::endsWith);
    }

}
