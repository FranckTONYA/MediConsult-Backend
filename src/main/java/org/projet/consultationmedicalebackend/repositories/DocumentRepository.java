package org.projet.consultationmedicalebackend.repositories;

import org.projet.consultationmedicalebackend.models.Document;
import org.projet.consultationmedicalebackend.models.Consultation;
import org.projet.consultationmedicalebackend.models.DossierMedical;
import org.projet.consultationmedicalebackend.models.Ordonnance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByConsultation(Consultation consultation);

    List<Document> findByOrdonnance(Ordonnance ordonnance);

    List<Document> findByDossierMedical(DossierMedical dossierMedical);
}
