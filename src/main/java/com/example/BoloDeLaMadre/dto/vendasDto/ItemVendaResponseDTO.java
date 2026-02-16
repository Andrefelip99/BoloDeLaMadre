package com.example.BoloDeLaMadre.dto.vendasDto;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.BoloDeLaMadre.entities.Produto;
import com.example.BoloDeLaMadre.entities.vendas.ItemVenda;

import lombok.Getter;

@Getter
public class ItemVendaResponseDTO {

    private UUID id;
    private UUID vendaId;
    private UUID produtoId;
    private String produtoNome;
    private Double quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal custoUnitario;
    private BigDecimal subtotal;

    public ItemVendaResponseDTO(ItemVenda entity) {
        this.id = entity.getId();
        this.vendaId = entity.getVenda() != null ? entity.getVenda().getId() : null;

        Produto produto = entity.getProduto();
        this.produtoId = produto != null ? produto.getId() : null;
        this.produtoNome = produto != null ? produto.getNome() : null;

        this.quantidade = entity.getQuantidade();
        this.precoUnitario = entity.getPrecoUnitario();
        this.custoUnitario = entity.getCustoUnitario();
        this.subtotal = entity.getSubtotal();
    }
}
