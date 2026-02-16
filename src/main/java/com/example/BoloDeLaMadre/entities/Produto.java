package com.example.BoloDeLaMadre.entities;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "produtos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Produto extends BaseEntity {

    private String nome;
    private String descricao;
    private BigDecimal preco;
    private BigDecimal custoEstimado;
    private BigDecimal margemLucro;
    private String tamanho;
    private Double pesoKg;
    private String fotoUrl;
    private Boolean ativo;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

}
