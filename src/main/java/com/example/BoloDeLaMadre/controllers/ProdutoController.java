package com.example.BoloDeLaMadre.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.example.BoloDeLaMadre.dto.ProdutoRequestDTO;
import com.example.BoloDeLaMadre.dto.ProdutoResponseDTO;
import com.example.BoloDeLaMadre.entities.Produto;
import com.example.BoloDeLaMadre.services.ProdutoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoResponseDTO create(@RequestBody @Valid ProdutoRequestDTO dto) {

        Produto produto = produtoService.create(dto);

        return new ProdutoResponseDTO(produto);
    }

    @PutMapping("/{id}")
    public ProdutoResponseDTO update(@PathVariable UUID id,
                                     @RequestBody @Valid ProdutoRequestDTO dto) {

        Produto produto = produtoService.update(id, dto);

        return new ProdutoResponseDTO(produto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        produtoService.delete(id);
    }

    @GetMapping("/{id}")
    public ProdutoResponseDTO getById(@PathVariable UUID id) {
        return new ProdutoResponseDTO(produtoService.getById(id));
    }

    @GetMapping
    public List<ProdutoResponseDTO> listAll() {
        return produtoService.listAll()
                .stream()
                .map(ProdutoResponseDTO::new)
                .toList();
    }
}
