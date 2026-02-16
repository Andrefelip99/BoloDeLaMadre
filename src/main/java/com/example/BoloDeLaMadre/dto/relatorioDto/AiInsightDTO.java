package com.example.BoloDeLaMadre.dto.relatorioDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AiInsightDTO {

    public enum TipoInsight {
        POSITIVO,
        ALERTA,
        OPORTUNIDADE
    }

    private TipoInsight tipo;
    private String titulo;
    private String descricao;
    private String metrica; 
    private String acao; 
}
