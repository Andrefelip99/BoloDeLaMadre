package com.example.BoloDeLaMadre.dto.financeiroDto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.BoloDeLaMadre.entities.enums.TipoFinanceiro;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class  LancamentoFinanceiroRequestDTO {
    
    private TipoFinanceiro tipo;
    private String categoria;
    private String descricao;
    @NotNull(message = "O valor é obrigatório")
    @Positive(message = "O valor deve ser maior que zero")
    private BigDecimal valor;
    private LocalDate dataLancamento;
    
}
