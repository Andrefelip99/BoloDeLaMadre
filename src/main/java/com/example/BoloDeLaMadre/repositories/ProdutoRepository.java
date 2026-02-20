package com.example.BoloDeLaMadre.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.BoloDeLaMadre.entities.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, UUID> {

    @Query("SELECT p FROM Produto p JOIN FETCH p.categoria WHERE p.id = :id")
    Optional<Produto> findByIdWithCategoria(@Param("id") UUID id);

    @Query("SELECT p FROM Produto p JOIN FETCH p.categoria")
    List<Produto> findAllWithCategoria();

}
