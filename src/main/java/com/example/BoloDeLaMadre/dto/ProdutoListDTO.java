package com.example.BoloDeLaMadre.dto;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProdutoListDTO {

    private UUID id;
    private String nome;
    private BigDecimal preco;
    private Boolean ativo;
    private String categoriaNome;
}
