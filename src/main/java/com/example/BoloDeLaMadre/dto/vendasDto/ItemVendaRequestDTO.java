package com.example.BoloDeLaMadre.dto.vendasDto;

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
public class ItemVendaRequestDTO {

    private UUID vendaId;
    private UUID produtoId;
    private Double quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal custoUnitario;
    
}
