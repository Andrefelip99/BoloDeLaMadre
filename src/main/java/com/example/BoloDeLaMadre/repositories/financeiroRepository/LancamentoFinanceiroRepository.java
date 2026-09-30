package com.example.BoloDeLaMadre.repositories.financeiroRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.BoloDeLaMadre.entities.enums.TipoFinanceiro;
import com.example.BoloDeLaMadre.entities.financeiro.LancamentoFinanceiro;

public interface LancamentoFinanceiroRepository extends JpaRepository<LancamentoFinanceiro, UUID> {
     @Query("SELECT l FROM LancamentoFinanceiro l WHERE l.dataLancamento >= :inicio AND l.dataLancamento < :fim")
    List<LancamentoFinanceiro> findByPeriodo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

    @Query("SELECT SUM(l.valor) FROM LancamentoFinanceiro l WHERE l.tipo = :tipo AND l.dataLancamento >= :inicio AND l.dataLancamento < :fim")
    BigDecimal sumByTipoAndPeriodo(@Param("tipo") TipoFinanceiro tipo, @Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);
}
