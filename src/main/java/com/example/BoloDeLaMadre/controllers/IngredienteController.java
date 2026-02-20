package com.example.BoloDeLaMadre.controllers;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.example.BoloDeLaMadre.entities.Ingrediente;
import com.example.BoloDeLaMadre.entities.enums.UnidadeMedida;
import com.example.BoloDeLaMadre.services.IngredienteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ingredientes")
@RequiredArgsConstructor
public class IngredienteController {

    private final IngredienteService ingredienteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Ingrediente create(@RequestParam String nome,
                              @RequestParam UnidadeMedida unidade,
                              @RequestParam BigDecimal custoUnitario,
                              @RequestParam Double estoqueAtual,
                              @RequestParam Double estoqueMinimo,
                              @RequestParam(required = false) UUID fornecedorId) {

        return ingredienteService.create(
                nome,
                unidade,
                custoUnitario,
                estoqueAtual,
                estoqueMinimo,
                fornecedorId
        );
    }

    @PutMapping("/{id}")
    public Ingrediente update(@PathVariable UUID id,
                              @RequestParam String nome,
                              @RequestParam UnidadeMedida unidade,
                              @RequestParam BigDecimal custoUnitario,
                              @RequestParam Double estoqueAtual,
                              @RequestParam Double estoqueMinimo,
                              @RequestParam(required = false) UUID fornecedorId) {

        return ingredienteService.update(
                id,
                nome,
                unidade,
                custoUnitario,
                estoqueAtual,
                estoqueMinimo,
                fornecedorId
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        ingredienteService.delete(id);
    }

    @GetMapping("/{id}")
    public Ingrediente getById(@PathVariable UUID id) {
        return ingredienteService.getById(id);
    }

    @GetMapping
    public List<Ingrediente> listAll() {
        return ingredienteService.listAll();
    }
}
