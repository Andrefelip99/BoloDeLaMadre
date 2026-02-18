package com.example.BoloDeLaMadre.repositories.iaRepository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.BoloDeLaMadre.entities.ia.AiMensagem;

public interface AiMensagemRepository extends JpaRepository<AiMensagem, UUID> {
    @Query("SELECT m FROM AiMensagem m JOIN FETCH m.conversa c WHERE c.id = :conversaId")
    List<AiMensagem> findByConversaIdWithConversa(@Param("conversaId") UUID conversaId);

}
