package com.example.BoloDeLaMadre.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BoloDeLaMadre.entities.Fornecedor;

public interface FornecedorRepository extends JpaRepository<Fornecedor, UUID> {
}
