package com.example.BoloDeLaMadre.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "categorias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categoria extends BaseEntity {

    private String nome;
    private String descricao;
    private Boolean ativo;

    public Categoria(String nome, String descricao, boolean ativa) {
        this.nome = nome;
        this.descricao = descricao;
        this.ativo = ativa;
    }

}
