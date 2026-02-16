package com.example.BoloDeLaMadre.dto.financeiroDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.example.BoloDeLaMadre.entities.financeiro.LancamentoFinanceiro;
import com.example.BoloDeLaMadre.entities.enums.TipoFinanceiro;

import lombok.Getter;

@Getter
public class LancamentoFinanceiroResponseDTO {

    private UUID id;
    private TipoFinanceiro tipo;
    private String categoria;
    private String descricao;
    private BigDecimal valor;
    private LocalDate dataLancamento;
    private LocalDate dataPagamento;
    private Boolean pago;
    private UUID referenciaId;
    private String observacoes;

    public LancamentoFinanceiroResponseDTO(LancamentoFinanceiro entity) {
        this.id = entity.getId();
        this.tipo = entity.getTipo();
        this.categoria = entity.getCategoria();
        this.descricao = entity.getDescricao();
        this.valor = entity.getValor();
        this.dataLancamento = entity.getDataLancamento();
        this.dataPagamento = entity.getDataPagamento();
        this.pago = entity.getPago(); 
        this.referenciaId = entity.getReferenciaId();
        this.observacoes = entity.getObservacoes();
    }
}
