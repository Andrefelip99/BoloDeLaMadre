package com.example.BoloDeLaMadre.dto;

import java.util.UUID;

import com.example.BoloDeLaMadre.entities.Categoria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaResponseDTO {

    private UUID id;
    private String nome;
    private String descricao;
    private Boolean ativo;

    public CategoriaResponseDTO(Categoria entity){
        this.id = entity.getId();
        this.nome = entity.getNome();
        this.descricao = entity.getDescricao();
        this.ativo = entity.getAtivo();
    }
}
