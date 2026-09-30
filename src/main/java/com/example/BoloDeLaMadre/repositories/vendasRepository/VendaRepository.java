package com.example.BoloDeLaMadre.repositories.vendasRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.time.LocalDateTime;

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

     @Query("SELECT v FROM Venda v WHERE v.dataVenda >= :inicio AND v.dataVenda < :fim")
    List<Venda> findByPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    @Query("SELECT SUM(v.total) FROM Venda v WHERE v.dataVenda >= :inicio AND v.dataVenda < :fim")
    BigDecimal sumTotalByPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

}
