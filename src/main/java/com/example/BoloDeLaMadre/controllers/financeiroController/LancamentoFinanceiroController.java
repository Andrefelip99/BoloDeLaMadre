package com.example.BoloDeLaMadre.controllers.financeiroController;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.example.BoloDeLaMadre.dto.financeiroDto.LancamentoFinanceiroRequestDTO;
import com.example.BoloDeLaMadre.dto.financeiroDto.LancamentoFinanceiroResponseDTO;
import com.example.BoloDeLaMadre.dto.financeiroDto.ResumoFinanceiroDTO;
import com.example.BoloDeLaMadre.services.financeiroService.LancamentoFinanceiroService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/lancamentos-financeiros")
@RequiredArgsConstructor
public class LancamentoFinanceiroController {

    private final LancamentoFinanceiroService lancamentoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LancamentoFinanceiroResponseDTO create(@RequestBody LancamentoFinanceiroRequestDTO dto) {
        return lancamentoService.create(dto);
    }

    @GetMapping
    public List<LancamentoFinanceiroResponseDTO> listAll() {
        return lancamentoService.listAll();
    }

    @GetMapping("/resumo")
    public ResumoFinanceiroDTO resumo() {
        return lancamentoService.resumo();
    }
}
