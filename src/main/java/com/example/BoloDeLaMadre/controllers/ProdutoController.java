package com.example.BoloDeLaMadre.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.example.BoloDeLaMadre.entities.Produto;
import com.example.BoloDeLaMadre.services.ProdutoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Produto create(@RequestBody Produto produto,
                          @RequestParam(required = false) UUID categoriaId) {

        return produtoService.create(produto, categoriaId);
    }

    @PutMapping("/{id}")
    public Produto update(@PathVariable UUID id,
                          @RequestBody Produto produto,
                          @RequestParam(required = false) UUID categoriaId) {

        return produtoService.update(id, produto, categoriaId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        produtoService.delete(id);
    }

    @GetMapping("/{id}")
    public Produto getById(@PathVariable UUID id) {
        return produtoService.getById(id);
    }

    @GetMapping
    public List<Produto> listAll() {
        return produtoService.listAll();
    }
}
