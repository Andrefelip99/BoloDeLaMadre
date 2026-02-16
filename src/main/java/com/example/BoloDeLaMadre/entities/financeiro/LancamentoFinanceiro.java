package com.example.BoloDeLaMadre.entities.financeiro;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.example.BoloDeLaMadre.entities.BaseEntity;
import com.example.BoloDeLaMadre.entities.enums.TipoFinanceiro;

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
@Table(name = "lancamentos_financeiros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LancamentoFinanceiro extends BaseEntity {


    @Enumerated(EnumType.STRING)
    private TipoFinanceiro tipo;

    private String categoria;
    private String descricao;
    private BigDecimal valor;

    private LocalDate dataLancamento;
    private LocalDate dataPagamento;

    private Boolean pago;
    private UUID referenciaId;
    private String observacoes;


}
