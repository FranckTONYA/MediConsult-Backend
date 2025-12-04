package org.projet.consultationmedicalebackend.services.impl;

import org.projet.consultationmedicalebackend.models.*;
import org.projet.consultationmedicalebackend.repositories.ConsultationRepository;
import org.projet.consultationmedicalebackend.repositories.DocumentRepository;
import org.projet.consultationmedicalebackend.repositories.DossierMedicalRepository;
import org.projet.consultationmedicalebackend.repositories.OrdonnanceRepository;
import org.projet.consultationmedicalebackend.services.DocumentService;
import org.projet.consultationmedicalebackend.utils.CustomResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final ConsultationRepository consultationRepository;
    private final OrdonnanceRepository ordonnanceRepository;
    private final DossierMedicalRepository dossierMedicalRepository;

    public DocumentServiceImpl(DocumentRepository documentRepository, ConsultationRepository consultationRepository,
                               OrdonnanceRepository ordonnanceRepository, DossierMedicalRepository dossierMedicalRepository) {
        this.documentRepository = documentRepository;
        this.consultationRepository = consultationRepository;
        this.ordonnanceRepository = ordonnanceRepository;
        this.dossierMedicalRepository = dossierMedicalRepository;
    }

    @Override
    public Document save(Document document) {
        return documentRepository.save(document);
    }

    @Override
    public List<Document> findAll() {
        return documentRepository.findAll();
    }

    @Override
    public Optional<Document> findById(Long id) {
        return documentRepository.findById(id);
    }

    @Override
    public List<Document> findByConsultation(Long consultationId) {
        Consultation consultation = consultationRepository.findById(consultationId).orElse(null);
        return documentRepository.findByConsultation(consultation);
    }

    @Override
    public List<Document> findByOrdonnance(Long ordonnanceId) {
        Ordonnance ordonnance = ordonnanceRepository.findById(ordonnanceId).orElse(null);
        return documentRepository.findByOrdonnance(ordonnance);
    }

    @Override
    public List<Document> findByDossierMedical(Long dossierId) {
        DossierMedical dossierMedical = dossierMedicalRepository.findById(dossierId).orElse(null);
        return documentRepository.findByDossierMedical((dossierMedical));
    }

    @Override
    public void delete(Long id) {
        documentRepository.deleteById(id);
    }

    @Override
    public CustomResponse uploadConsultationFiles(List<MultipartFile> files, Consultation consultation) {
        CustomResponse response = new CustomResponse();
        try {
            // Créer le dossier si inexistant
            String uploadDir = System.getProperty("user.dir") + "/uploads-consultations/";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            List<Document> savedDocuments = new ArrayList<>();
            for (MultipartFile file : files) {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path dest = uploadPath.resolve(fileName);
                file.transferTo(dest.toFile());

                TypeDoc type = file.getContentType().contains("image") ? TypeDoc.IMAGE : TypeDoc.PDF;
                Document doc = new Document(fileName, type, fileName, consultation);
                savedDocuments.add(documentRepository.save(doc));
            }

            response.status = true;
            response.message = "Consultation mise à jour";
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            response.status = false;
            response.message = e.getMessage();
            return response;
        }
    }
}
