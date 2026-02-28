package com.example.BoloDeLaMadre.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.example.BoloDeLaMadre.dto.CategoriaRequestDTO;
import com.example.BoloDeLaMadre.dto.CategoriaResponseDTO;
import com.example.BoloDeLaMadre.services.CategoriaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaResponseDTO create(@Valid @RequestBody CategoriaRequestDTO dto) {
        return categoriaService.create(dto);
    }

    @PutMapping("/{id}")
    public CategoriaResponseDTO update(@Valid @PathVariable UUID id,
                                       @RequestBody CategoriaRequestDTO dto) {
        return categoriaService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        categoriaService.delete(id);
    }

    @GetMapping("/{id}")
    public CategoriaResponseDTO getById(@PathVariable UUID id) {
        return categoriaService.getById(id);
    }

    @GetMapping
    public List<CategoriaResponseDTO> listAll() {
        return categoriaService.listAll();
    }
}
