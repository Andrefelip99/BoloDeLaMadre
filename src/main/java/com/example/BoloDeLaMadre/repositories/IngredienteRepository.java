package com.example.BoloDeLaMadre.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.BoloDeLaMadre.entities.Ingrediente;

public interface IngredienteRepository extends JpaRepository<Ingrediente, UUID> {

    
    @Query("SELECT i FROM Ingrediente i JOIN FETCH i.fornecedor WHERE i.id = :id")
    Ingrediente findByIdWithFornecedor(@Param("id") UUID id);

    @Query("SELECT i FROM Ingrediente i JOIN FETCH i.fornecedor")
    List<Ingrediente> findAllWithFornecedor();

    
    @Query("SELECT COUNT(i) FROM Ingrediente i WHERE i.estoqueAtual < i.estoqueMinimo")
    long countByEstoqueAtualLessThanEstoqueMinimo();

    
}
