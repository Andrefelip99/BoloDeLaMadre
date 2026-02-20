package com.example.BoloDeLaMadre.repositories.financeiroRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.BoloDeLaMadre.entities.enums.TipoFinanceiro;
import com.example.BoloDeLaMadre.entities.financeiro.LancamentoFinanceiro;

public interface LancamentoFinanceiroRepository extends JpaRepository<LancamentoFinanceiro, UUID> {
     @Query("SELECT l FROM LancamentoFinanceiro l WHERE FUNCTION('YEAR', l.dataLancamento) = :ano AND FUNCTION('MONTH', l.dataLancamento) = :mes")
    List<LancamentoFinanceiro> findByMesEAno(@Param("mes") int mes, @Param("ano") int ano);

    @Query("SELECT SUM(l.valor) FROM LancamentoFinanceiro l WHERE l.tipo = :tipo AND FUNCTION('YEAR', l.dataLancamento) = :ano AND FUNCTION('MONTH', l.dataLancamento) = :mes")
    BigDecimal sumByTipoAndMesEAno(@Param("tipo") TipoFinanceiro tipo, @Param("mes") int mes, @Param("ano") int ano);
}
