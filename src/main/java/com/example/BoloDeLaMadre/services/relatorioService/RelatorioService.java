package com.example.BoloDeLaMadre.services.relatorioService;


import java.util.List;

import org.springframework.stereotype.Service;

import com.example.BoloDeLaMadre.dto.relatorioDto.AiInsightDTO;
import com.example.BoloDeLaMadre.dto.relatorioDto.DreDataDTO;
import com.example.BoloDeLaMadre.dto.relatorioDto.KpiDataDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RelatorioService {


    public DreDataDTO gerarDre() {
        return new DreDataDTO();
    }

    public KpiDataDTO gerarKpi() {
        return new KpiDataDTO();
    }

    public List<AiInsightDTO> gerarInsights() {
        return List.of();
    }
}
