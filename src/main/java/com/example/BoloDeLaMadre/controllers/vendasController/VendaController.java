package com.example.BoloDeLaMadre.controllers.vendasController;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.example.BoloDeLaMadre.dto.vendasDto.VendaDetailsDTO;
import com.example.BoloDeLaMadre.dto.vendasDto.VendaRequestDTO;
import com.example.BoloDeLaMadre.services.vendaService.VendaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/vendas")
@RequiredArgsConstructor
public class VendaController {

    private final VendaService vendaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VendaDetailsDTO create(@RequestBody VendaRequestDTO dto) {
        return vendaService.create(dto);
    }

    @GetMapping("/{id}")
    public VendaDetailsDTO getById(@PathVariable UUID id) {
        return vendaService.getById(id);
    }

    @GetMapping
    public List<VendaDetailsDTO> listAll() {
        return vendaService.listAll();
    }
}
