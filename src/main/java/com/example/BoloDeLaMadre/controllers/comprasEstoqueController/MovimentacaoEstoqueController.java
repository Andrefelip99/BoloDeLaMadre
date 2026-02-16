package com.example.BoloDeLaMadre.controllers.comprasEstoqueController;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.example.BoloDeLaMadre.entities.comprasEstoque.MovimentacaoEstoque;
import com.example.BoloDeLaMadre.services.comprasEstoqueService.MovimentacaoEstoqueService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/movimentacoes-estoque")
@RequiredArgsConstructor
public class MovimentacaoEstoqueController {

    private final MovimentacaoEstoqueService movimentacaoEstoqueService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovimentacaoEstoque create(@RequestBody MovimentacaoEstoque movimentacao) {
        return movimentacaoEstoqueService.create(movimentacao);
    }

    @GetMapping
    public List<MovimentacaoEstoque> listAll() {
        return movimentacaoEstoqueService.listAll();
    }
}
