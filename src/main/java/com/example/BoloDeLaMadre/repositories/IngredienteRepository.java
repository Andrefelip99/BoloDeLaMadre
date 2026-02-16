package com.example.BoloDeLaMadre.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BoloDeLaMadre.entities.Ingrediente;

public interface IngredienteRepository extends JpaRepository<Ingrediente, UUID> {
}
