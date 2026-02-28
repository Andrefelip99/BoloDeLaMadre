package com.example.BoloDeLaMadre.controllers.comprasEstoqueController;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.example.BoloDeLaMadre.dto.comprasEstoqueDto.CompraRequestDTO;
import com.example.BoloDeLaMadre.dto.comprasEstoqueDto.CompraWithItemsDTO;
import com.example.BoloDeLaMadre.services.comprasEstoqueService.CompraService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/compras")
@RequiredArgsConstructor
public class CompraController {

    private final CompraService compraService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompraWithItemsDTO create(@RequestBody CompraRequestDTO dto) {
        return compraService.create(dto);
    }

    @GetMapping("/{id}")
    public CompraWithItemsDTO getById(@PathVariable UUID id) {
        return compraService.getById(id);
    }

    @GetMapping
    public List<CompraWithItemsDTO> listAll() {
        return compraService.listAll();
    }
}
