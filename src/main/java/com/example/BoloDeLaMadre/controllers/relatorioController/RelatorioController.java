package com.example.BoloDeLaMadre.controllers.relatorioController;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.BoloDeLaMadre.dto.relatorioDto.AiInsightDTO;
import com.example.BoloDeLaMadre.dto.relatorioDto.DreDataDTO;
import com.example.BoloDeLaMadre.dto.relatorioDto.KpiDataDTO;
import com.example.BoloDeLaMadre.services.relatorioService.RelatorioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/relatorios")
@RequiredArgsConstructor
public class RelatorioController {

    private final RelatorioService relatorioService;

    @GetMapping("/dre")
    public ResponseEntity<DreDataDTO> gerarDre() {
        DreDataDTO dre = relatorioService.gerarDre();
        return ResponseEntity.ok(dre);
    }

    @GetMapping("/kpi")
    public ResponseEntity<KpiDataDTO> gerarKpi() {
        KpiDataDTO kpi = relatorioService.gerarKpi();
        return ResponseEntity.ok(kpi);
    }

    @GetMapping("/insights")
    public ResponseEntity<List<AiInsightDTO>> gerarInsights() {
        List<AiInsightDTO> insights = relatorioService.gerarInsights();
        return ResponseEntity.ok(insights);
    }
}
