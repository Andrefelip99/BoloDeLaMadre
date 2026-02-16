package com.example.BoloDeLaMadre.entities;

import java.math.BigDecimal;

import com.example.BoloDeLaMadre.entities.enums.PapelUsuario;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "funcionarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Funcionario extends BaseEntity {

    private String nome;
    private String email;
    private String telefone;

    @Enumerated(EnumType.STRING)
    private PapelUsuario papel;

    private BigDecimal salario;
    private Boolean ativo;

}
