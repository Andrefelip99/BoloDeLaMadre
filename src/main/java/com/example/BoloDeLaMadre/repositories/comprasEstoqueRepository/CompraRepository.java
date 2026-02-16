package com.example.BoloDeLaMadre.repositories.comprasEstoqueRepository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BoloDeLaMadre.entities.comprasEstoque.Compra;

public interface CompraRepository  extends JpaRepository<Compra, UUID> {
}
