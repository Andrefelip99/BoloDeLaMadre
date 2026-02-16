package com.example.BoloDeLaMadre.repositories.financeiroRepository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BoloDeLaMadre.entities.financeiro.LancamentoFinanceiro;

public interface LancamentoFinanceiroRepository extends JpaRepository<LancamentoFinanceiro, UUID> {
}
