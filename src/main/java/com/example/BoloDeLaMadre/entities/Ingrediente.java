package com.example.BoloDeLaMadre.entities;

import java.math.BigDecimal;

import com.example.BoloDeLaMadre.entities.enums.UnidadeMedida;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ingredientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ingrediente extends BaseEntity {

    private String nome;

    @Enumerated(EnumType.STRING)
    private UnidadeMedida unidade;

    private BigDecimal custoUnitario;
    private Double estoqueAtual;
    private Double estoqueMinimo;
    private Boolean ativo;

    @ManyToOne
    @JoinColumn(name = "fornecedor_id")
    private Fornecedor fornecedor;

    
}
