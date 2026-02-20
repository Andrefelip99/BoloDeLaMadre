package com.example.BoloDeLaMadre.controllers.vendasController;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.BoloDeLaMadre.dto.vendasDto.ItemVendaRequestDTO;
import com.example.BoloDeLaMadre.dto.vendasDto.ItemVendaResponseDTO;
import com.example.BoloDeLaMadre.services.vendaService.ItemVendaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/itens-venda")
@RequiredArgsConstructor
public class ItemVendaController {

    private final ItemVendaService itemVendaService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO')")
    @ResponseStatus(HttpStatus.CREATED)
    public ItemVendaResponseDTO create(@RequestBody ItemVendaRequestDTO dto) {
        return itemVendaService.create(dto);
    }

    @PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO')")
    @GetMapping("/venda/{vendaId}")
    public List<ItemVendaResponseDTO> listByVenda(@PathVariable UUID vendaId) {
        return itemVendaService.listByVenda(vendaId);
    }
}
