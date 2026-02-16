package com.example.BoloDeLaMadre.dto.comprasEstoqueDto;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemCompraRequestDTO {

    private UUID ingredienteId;
    private BigDecimal quantidade;
    private BigDecimal precoUnitario;
}
