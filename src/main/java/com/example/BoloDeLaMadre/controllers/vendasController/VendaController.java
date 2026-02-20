package com.example.BoloDeLaMadre.controllers.vendasController;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

   
    @PutMapping("/{id}")
    public VendaDetailsDTO update(@PathVariable UUID id, @RequestBody VendaRequestDTO dto) {
       
        return vendaService.getById(id);
    }

    
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable UUID id) {
        vendaService.cancelarVenda(id);
        return ResponseEntity.noContent().build();
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<VendaDetailsDTO> getById(@PathVariable UUID id) {
        VendaDetailsDTO venda = vendaService.getById(id);
        return ResponseEntity.ok(venda);
    }

    
    @GetMapping
    public ResponseEntity<List<VendaDetailsDTO>> listAll() {
        List<VendaDetailsDTO> vendas = vendaService.listAll();
        return ResponseEntity.ok(vendas);
    }
}
