package com.example.BoloDeLaMadre.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.BoloDeLaMadre.entities.Receita;

public interface ReceitaRepository extends JpaRepository<Receita, UUID> {

    
    @Query("SELECT r FROM Receita r " +
           "JOIN FETCH r.produto " +
           "JOIN FETCH r.ingrediente " +
           "WHERE r.id = :id")
    Optional<Receita> findByIdWithProdutoAndIngrediente(@Param("id") UUID id);

   
    @Query("""
       SELECT r FROM Receita r
       JOIN FETCH r.produto
       JOIN FETCH r.ingrediente
       WHERE r.ativo = true
       """)
List<Receita> findAllWithProdutoAndIngrediente();

}
