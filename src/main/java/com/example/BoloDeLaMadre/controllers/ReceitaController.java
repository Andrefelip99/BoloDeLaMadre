package com.example.BoloDeLaMadre.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.example.BoloDeLaMadre.entities.Receita;
import com.example.BoloDeLaMadre.entities.enums.UnidadeMedida;
import com.example.BoloDeLaMadre.services.ReceitaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/receitas")
@RequiredArgsConstructor
public class ReceitaController {

    private final ReceitaService receitaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Receita create(@RequestParam UUID produtoId,
                          @RequestParam UUID ingredienteId,
                          @RequestParam Double quantidade,
                          @RequestParam UnidadeMedida unidade) {

        return receitaService.create(produtoId, ingredienteId, quantidade, unidade);
    }

    @PutMapping("/{id}")
    public Receita update(@PathVariable UUID id,
                          @RequestParam UUID produtoId,
                          @RequestParam UUID ingredienteId,
                          @RequestParam Double quantidade,
                          @RequestParam UnidadeMedida unidade) {

        return receitaService.update(id, produtoId, ingredienteId, quantidade, unidade);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        receitaService.delete(id);
    }

    @GetMapping("/{id}")
    public Receita getById(@PathVariable UUID id) {
        return receitaService.getById(id);
    }

    @GetMapping
    public List<Receita> listAll() {
        return receitaService.listAll();
    }
}
