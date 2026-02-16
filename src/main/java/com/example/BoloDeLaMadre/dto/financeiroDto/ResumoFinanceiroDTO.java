package com.example.BoloDeLaMadre.dto.financeiroDto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResumoFinanceiroDTO {

    private BigDecimal totalReceitas;
    private BigDecimal totalDespesas;
    private BigDecimal lucro;
}

