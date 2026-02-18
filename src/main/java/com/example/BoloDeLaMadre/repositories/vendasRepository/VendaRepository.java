package com.example.BoloDeLaMadre.repositories.vendasRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.BoloDeLaMadre.entities.vendas.Venda;

public interface VendaRepository extends JpaRepository<Venda, UUID> {

    @Query("SELECT v FROM Venda v " +
            "JOIN FETCH v.cliente " +
            "JOIN FETCH v.funcionario " +
            "LEFT JOIN FETCH v.itens " +
            "WHERE v.id = :id")
    Optional<Venda>findByIdWithDetails(@Param("id") UUID id);

    @Query("SELECT DISTINCT v FROM Venda v " +
            "JOIN FETCH v.cliente " +
            "JOIN FETCH v.funcionario " +
            "LEFT JOIN FETCH v.itens")
    List<Venda> findAllWithDetails();

}
