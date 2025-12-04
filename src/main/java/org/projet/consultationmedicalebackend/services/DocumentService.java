package org.projet.consultationmedicalebackend.services;

import org.projet.consultationmedicalebackend.models.Consultation;
import org.projet.consultationmedicalebackend.models.Document;
import org.projet.consultationmedicalebackend.utils.CustomResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface DocumentService {
    Document save(Document document);
    List<Document> findAll();
    Optional<Document> findById(Long id);
    List<Document> findByConsultation(Long consultationId);
    List<Document> findByOrdonnance(Long ordonnanceId);
    List<Document> findByDossierMedical(Long dossierId);
    void delete(Long id);
    CustomResponse uploadConsultationFiles(List<MultipartFile> file, Consultation consultation);
}
