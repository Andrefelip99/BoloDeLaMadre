package com.example.BoloDeLaMadre.controllers.relatorioController;

import java.time.YearMonth;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.BoloDeLaMadre.dto.relatorioDto.AiInsightDTO;
import com.example.BoloDeLaMadre.dto.relatorioDto.DreDataDTO;
import com.example.BoloDeLaMadre.dto.relatorioDto.KpiDataDTO;
import com.example.BoloDeLaMadre.services.relatorioService.RelatorioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/relatorios")
@RequiredArgsConstructor
public class RelatorioController {

    private final RelatorioService relatorioService;

    @GetMapping("/dre")
    public ResponseEntity<DreDataDTO> getDre(
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer ano) {

        YearMonth periodo = getPeriodo(mes, ano);
        DreDataDTO dre = relatorioService.gerarDre(periodo);
        return ResponseEntity.ok(dre);
    }

    @GetMapping("/kpis")
    public ResponseEntity<KpiDataDTO> getKpis(
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer ano) {

        YearMonth periodo = getPeriodo(mes, ano);
        KpiDataDTO kpi = relatorioService.gerarKpi(periodo);
        return ResponseEntity.ok(kpi);
    }

    @GetMapping("/insights")
    public ResponseEntity<List<AiInsightDTO>> getInsights(
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer ano) {

        YearMonth periodo = getPeriodo(mes, ano);
        List<AiInsightDTO> insights = relatorioService.gerarInsights(periodo);
        return ResponseEntity.ok(insights);
    }
    
    private YearMonth getPeriodo(Integer mes, Integer ano) {
        if (mes == null || ano == null) {
            return YearMonth.now();
        }
        return YearMonth.of(ano, mes);
    }
}
