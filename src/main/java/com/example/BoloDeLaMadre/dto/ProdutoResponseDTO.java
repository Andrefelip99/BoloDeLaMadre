package com.example.BoloDeLaMadre.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.BoloDeLaMadre.entities.Produto;

import lombok.Getter;

@Getter
public class ProdutoResponseDTO {
    private UUID id;
    private String nome;
    private BigDecimal preco;

    public ProdutoResponseDTO(Produto produto) {
        this.id = produto.getId();
        this.nome = produto.getNome();
        this.preco = produto.getPreco();
    }
}