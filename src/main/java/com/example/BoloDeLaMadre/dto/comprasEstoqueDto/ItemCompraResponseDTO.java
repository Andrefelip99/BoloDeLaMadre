package com.example.BoloDeLaMadre.dto.comprasEstoqueDto;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.BoloDeLaMadre.entities.comprasEstoque.ItemCompra;

import lombok.Getter;

@Getter
public class ItemCompraResponseDTO {

    private UUID id;
    private UUID compraId;
    private UUID ingredienteId;
    private double quantidade; // corrigido de Double para BigDecimal
    private BigDecimal precoUnitario;
    private BigDecimal subtotal;

    public ItemCompraResponseDTO(ItemCompra item) {
        this.id = item.getId();
        this.compraId = item.getCompra() != null ? item.getCompra().getId() : null;
        this.ingredienteId = item.getIngrediente() != null ? item.getIngrediente().getId() : null;
        this.quantidade = item.getQuantidade();
        this.precoUnitario = item.getPrecoUnitario();
        this.subtotal = item.getPrecoUnitario().multiply(
                BigDecimal.valueOf(item.getQuantidade().doubleValue()));

    }
}
