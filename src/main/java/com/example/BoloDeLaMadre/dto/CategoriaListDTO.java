package com.example.BoloDeLaMadre.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoriaListDTO {

    private UUID id;
    private String nome;
    private Boolean ativo;
}
