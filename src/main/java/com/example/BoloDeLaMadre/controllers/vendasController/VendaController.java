package com.example.BoloDeLaMadre.controllers.vendasController;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.BoloDeLaMadre.dto.vendasDto.VendaDetailsDTO;
import com.example.BoloDeLaMadre.dto.vendasDto.VendaRequestDTO;
import com.example.BoloDeLaMadre.services.vendaService.VendaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/vendas")
@RequiredArgsConstructor
public class VendaController {

    private final VendaService vendaService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public VendaDetailsDTO create(@RequestBody VendaRequestDTO dto) {
        return vendaService.create(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<VendaDetailsDTO> update(
            @PathVariable UUID id,
            @RequestBody VendaRequestDTO dto) {

        VendaDetailsDTO vendaAtualizada = vendaService.update(id, dto);
        return ResponseEntity.ok(vendaAtualizada);
    }

    @PatchMapping("/{id}/cancelar")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> cancelar(@PathVariable UUID id) {
        vendaService.cancelarVenda(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<VendaDetailsDTO> getById(@PathVariable UUID id) {
        VendaDetailsDTO venda = vendaService.getById(id);
        return ResponseEntity.ok(venda);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<List<VendaDetailsDTO>> listAll() {
        List<VendaDetailsDTO> vendas = vendaService.listAll();
        return ResponseEntity.ok(vendas);
    }
}
