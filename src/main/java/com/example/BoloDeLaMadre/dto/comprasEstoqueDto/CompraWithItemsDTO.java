package com.example.BoloDeLaMadre.dto.comprasEstoqueDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.example.BoloDeLaMadre.entities.comprasEstoque.Compra;

import lombok.Getter;
@Getter
public class CompraWithItemsDTO {

    private UUID id;
    private String numeroNota;
    private BigDecimal total;
    private LocalDateTime dataCompra;
    private String observacoes;
    private UUID fornecedorId;
    private String fornecedorNome;

    private List<ItemCompraResponseDTO> itens; 

    public CompraWithItemsDTO(Compra compra, List<ItemCompraResponseDTO> itensDTO) {
        this.id = compra.getId();
        this.numeroNota = compra.getNumeroNota();
        this.total = compra.getTotal();
        this.dataCompra = compra.getDataCompra();
        this.observacoes = compra.getObservacoes();

        if (compra.getFornecedor() != null) {
            this.fornecedorId = compra.getFornecedor().getId();
            this.fornecedorNome = compra.getFornecedor().getNome();
        }

        this.itens = itensDTO != null ? itensDTO : List.of();
    }
}
