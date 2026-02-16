package com.example.BoloDeLaMadre.dto.vendasDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.example.BoloDeLaMadre.entities.enums.CanalVenda;
import com.example.BoloDeLaMadre.entities.enums.FormaPagamento;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VendaRequestDTO {

    private UUID clienteId;
    private UUID funcionarioId;

    private CanalVenda canal;

    private List<ItemVendaRequestDTO> itens;

    private BigDecimal desconto;
    private BigDecimal taxaEntrega;

    private FormaPagamento formaPagamento;
}
