package com.example.BoloDeLaMadre.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente extends BaseEntity {

    private String nome;
    private String telefone;
    private String email;
    private String endereco;
    private LocalDate dataNascimento;
    private String observacoes;

    private BigDecimal totalCompras;
    private Integer quantidadePedidos;
    private LocalDateTime ultimaCompra;

}
