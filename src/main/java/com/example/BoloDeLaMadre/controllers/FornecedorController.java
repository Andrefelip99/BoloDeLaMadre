package com.example.BoloDeLaMadre.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Fornecedor create(@RequestBody Fornecedor fornecedor) {
        return fornecedorService.create(fornecedor);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Fornecedor update(@PathVariable UUID id,
                             @RequestBody Fornecedor fornecedor) {
        return fornecedorService.update(id, fornecedor);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        fornecedorService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Fornecedor> getById(@PathVariable UUID id) {
        Fornecedor fornecedor = fornecedorService.getById(id);
        return ResponseEntity.ok(fornecedor);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Fornecedor>> listAll() {
        List<Fornecedor> fornecedores = fornecedorService.listAll();
        return ResponseEntity.ok(fornecedores);
    }
}
