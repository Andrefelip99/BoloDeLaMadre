package com.example.BoloDeLaMadre.dto.relatorioDto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class DreDataDTO {

    private BigDecimal receitaBruta;
    private BigDecimal taxasPlataforma;
    private BigDecimal descontos;
    private BigDecimal receitaLiquida;
    private BigDecimal cmv;
    private BigDecimal lucroBruto;

    private DespesasOperacionais despesasOperacionais;

    private BigDecimal resultadoOperacional;
    private BigDecimal impostos;
    private BigDecimal resultadoLiquido;
    private BigDecimal margemBruta;
    private BigDecimal margemLiquida;

    @Getter
    @Setter
    public static class DespesasOperacionais {
        private BigDecimal aluguel;
        private BigDecimal energia;
        private BigDecimal agua;
        private BigDecimal gas;
        private BigDecimal funcionarios;
        private BigDecimal marketing;
        private BigDecimal embalagens;
        private BigDecimal equipamentos;
        private BigDecimal outros;
        private BigDecimal total;
    }
}
