package com.example.BoloDeLaMadre.repositories.iaRepository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BoloDeLaMadre.entities.ia.AiMensagem;

public interface AiMensagemRepository extends JpaRepository<AiMensagem, UUID> {
}
