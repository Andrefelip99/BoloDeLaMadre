package com.example.BoloDeLaMadre.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.example.BoloDeLaMadre.entities.Fornecedor;
import com.example.BoloDeLaMadre.services.FornecedorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/fornecedores")
@RequiredArgsConstructor
public class FornecedorController {

    private final FornecedorService fornecedorService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Fornecedor create(@RequestBody Fornecedor fornecedor) {
        return fornecedorService.create(fornecedor);
    }

    @PutMapping("/{id}")
    public Fornecedor update(@PathVariable UUID id,
                             @RequestBody Fornecedor fornecedor) {
        return fornecedorService.update(id, fornecedor);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        fornecedorService.delete(id);
    }

    @GetMapping("/{id}")
    public Fornecedor getById(@PathVariable UUID id) {
        return fornecedorService.getById(id);
    }

    @GetMapping
    public List<Fornecedor> listAll() {
        return fornecedorService.listAll();
    }
}
