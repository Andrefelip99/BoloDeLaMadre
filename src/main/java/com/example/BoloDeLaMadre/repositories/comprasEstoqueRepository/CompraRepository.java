package com.example.BoloDeLaMadre.repositories.comprasEstoqueRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.BoloDeLaMadre.entities.comprasEstoque.Compra;

public interface CompraRepository extends JpaRepository<Compra, UUID> {
    @Query("SELECT c FROM Compra c JOIN FETCH c.itens WHERE c.id = :id")
    Optional<Compra> findByIdWithItens(@Param("id") UUID id);

    @Query("SELECT DISTINCT c FROM Compra c JOIN FETCH c.itens")
    List<Compra> findAllWithItens();

}
