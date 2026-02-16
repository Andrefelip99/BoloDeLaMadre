package com.example.BoloDeLaMadre.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BoloDeLaMadre.entities.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
}
