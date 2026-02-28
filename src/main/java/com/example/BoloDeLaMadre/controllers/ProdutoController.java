package com.example.BoloDeLaMadre.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoResponseDTO create(@RequestBody @Valid ProdutoRequestDTO dto) {

        Produto produto = produtoService.create(dto);

        return new ProdutoResponseDTO(produto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ProdutoResponseDTO update(@PathVariable UUID id,
                                     @RequestBody @Valid ProdutoRequestDTO dto) {

        Produto produto = produtoService.update(id, dto);

        return new ProdutoResponseDTO(produto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        produtoService.delete(id);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ProdutoResponseDTO getById(@PathVariable UUID id) {
        return new ProdutoResponseDTO(produtoService.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public List<ProdutoResponseDTO> listAll() {
        return produtoService.listAll()
                .stream()
                .map(ProdutoResponseDTO::new)
                .toList();
    }
}
