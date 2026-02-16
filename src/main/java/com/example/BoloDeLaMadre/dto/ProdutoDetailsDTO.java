package com.example.BoloDeLaMadre.dto;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProdutoDetailsDTO {

    private UUID id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Boolean ativo;

    private UUID categoriaId;
    private String categoriaNome;
}
