package com.example.BoloDeLaMadre.repositories.iaRepository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.BoloDeLaMadre.entities.ia.AiConversa;

public interface AiConversaRepository extends JpaRepository<AiConversa, UUID> {


    @Query("SELECT c FROM AiConversa c LEFT JOIN FETCH c.mensagens WHERE c.id = :id")
    AiConversa findByIdWithMensagens(@Param("id") UUID id);

    @Query("SELECT DISTINCT c FROM AiConversa c LEFT JOIN FETCH c.mensagens")
    List<AiConversa> findAllWithMensagens();


}
