package com.example.BoloDeLaMadre.repositories.iaRepository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BoloDeLaMadre.entities.ia.AiConversa;

public interface AiConversaRepository extends JpaRepository<AiConversa, UUID> {
}
