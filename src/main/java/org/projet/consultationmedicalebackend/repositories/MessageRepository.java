package org.projet.consultationmedicalebackend.repositories;

import org.projet.consultationmedicalebackend.models.Message;
import org.projet.consultationmedicalebackend.models.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByEmetteur(Utilisateur emetteur);
    List<Message> findByRecepteur(Utilisateur recepteur);

    @Query("""
            SELECT m FROM Message m 
            WHERE 
                (m.emetteur = :u1 AND m.recepteur = :u2)
                OR 
                (m.emetteur = :u2 AND m.recepteur = :u1)
            ORDER BY m.dateEnvoi ASC
            """)
    List<Message> findConversation(@Param("u1") Utilisateur utilisateur1, @Param("u2") Utilisateur utilisateur2);

}
