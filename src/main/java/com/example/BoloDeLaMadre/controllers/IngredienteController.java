package com.example.BoloDeLaMadre.controllers;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.BoloDeLaMadre.entities.Ingrediente;
import com.example.BoloDeLaMadre.entities.enums.UnidadeMedida;
import com.example.BoloDeLaMadre.services.IngredienteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ingredientes")
@RequiredArgsConstructor
public class IngredienteController {

    private final IngredienteService ingredienteService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        ingredienteService.delete(id);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Ingrediente getById(@PathVariable UUID id) {
        return ingredienteService.getById(id);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public List<Ingrediente> listAll() {
        return ingredienteService.listAll();
    }
}
