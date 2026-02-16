package com.example.BoloDeLaMadre.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BoloDeLaMadre.entities.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, UUID> {
}
