package com.example.BoloDeLaMadre.repositories.vendasRepository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BoloDeLaMadre.entities.vendas.Venda;

public interface VendaRepository extends JpaRepository<Venda, UUID> {
}
