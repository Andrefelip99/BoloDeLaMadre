package com.example.BoloDeLaMadre.dto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoRequestDTO {

    @NotBlank
    private String nome;
    private String descricao;
    private UUID categoriaId;

    @NotNull
    private BigDecimal preco;
    private Boolean ativo;

}
