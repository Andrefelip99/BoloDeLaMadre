package com.example.BoloDeLaMadre.dto.financeiroDto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.BoloDeLaMadre.entities.enums.TipoFinanceiro;

import lombok.Data;

@Data
public class  LancamentoFinanceiroRequestDTO {
    
    private TipoFinanceiro tipo;
    private String categoria;
    private String descricao;
    private BigDecimal valor;
    private LocalDate dataLancamento;
    
}