package com.example.BoloDeLaMadre.dto.relatorioDto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class KpiDataDTO {

    private BigDecimal receitaMes;
    private BigDecimal receitaMesAnterior;
    private Long quantidadeVendas;
    private BigDecimal ticketMedio;
    private BigDecimal margemLucro;
    private Long itensEstoqueBaixo;
    private String topCanal;
    private String topProduto;
}
