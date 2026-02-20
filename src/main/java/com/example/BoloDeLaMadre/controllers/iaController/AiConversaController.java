package com.example.BoloDeLaMadre.controllers.iaController;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.BoloDeLaMadre.dto.iaDto.AiMensagemResponseDTO;
import com.example.BoloDeLaMadre.entities.ia.AiConversa;
import com.example.BoloDeLaMadre.services.iaService.AiConversaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ai-conversas")
@RequiredArgsConstructor
public class AiConversaController {

    private final AiConversaService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO')")
    public AiConversa create(@RequestParam String titulo) {
        return service.create(titulo);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO')")
    public List<AiConversa> list() {
        return service.list();
    }

    @GetMapping("/{id}/mensagens")
    @PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO')")
    public List<AiMensagemResponseDTO> listMensagens(@PathVariable UUID id) {
        return service.listMensagens(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO')")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
