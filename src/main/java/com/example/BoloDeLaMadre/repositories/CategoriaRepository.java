package com.example.BoloDeLaMadre.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BoloDeLaMadre.entities.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, UUID> {
    List<Categoria> findByAtivoTrue();

}
