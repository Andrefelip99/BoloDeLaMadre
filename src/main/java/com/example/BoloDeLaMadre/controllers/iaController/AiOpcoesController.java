package com.example.BoloDeLaMadre.controllers.iaController;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BoloDeLaMadre.services.iaService.BusinessContextService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ia/opcoes")
@RequiredArgsConstructor
public class AiOpcoesController {

    private final BusinessContextService businessContextService;

    @GetMapping("/produtos")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<String> produtosAtivos() {
        return businessContextService.activeProductNames();
    }
}
