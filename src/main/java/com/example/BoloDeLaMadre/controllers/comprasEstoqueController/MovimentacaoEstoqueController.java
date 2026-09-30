package com.example.BoloDeLaMadre.controllers.comprasEstoqueController;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.BoloDeLaMadre.entities.comprasEstoque.MovimentacaoEstoque;
import com.example.BoloDeLaMadre.services.comprasEstoqueService.MovimentacaoEstoqueService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/movimentacoes-estoque")
@RequiredArgsConstructor
public class MovimentacaoEstoqueController {

    private final MovimentacaoEstoqueService movimentacaoEstoqueService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public MovimentacaoEstoque create(@RequestBody MovimentacaoEstoque movimentacao) {
        return movimentacaoEstoqueService.create(movimentacao);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<MovimentacaoEstoque> listAll() {
        return movimentacaoEstoqueService.listAll();
    }
}
