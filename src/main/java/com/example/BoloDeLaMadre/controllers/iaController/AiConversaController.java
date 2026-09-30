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
@RequestMapping("/api/ai-conversas")
@RequiredArgsConstructor
public class AiConversaController {

    private final AiConversaService service;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public AiConversa create(@RequestParam String titulo) {
        return service.create(titulo);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<AiConversa> list() {
        return service.list();
    }

    @GetMapping("/{id}/mensagens")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<AiMensagemResponseDTO> listMensagens(@PathVariable UUID id) {
        return service.listMensagens(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
