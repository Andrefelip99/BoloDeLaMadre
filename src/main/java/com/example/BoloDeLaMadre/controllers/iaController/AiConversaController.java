package com.example.BoloDeLaMadre.controllers.iaController;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.example.BoloDeLaMadre.dto.iaDto.AiMensagemResponseDTO;
import com.example.BoloDeLaMadre.entities.ia.AiConversa;
import com.example.BoloDeLaMadre.services.iaService.AiConversaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ai-conversas")
@RequiredArgsConstructor
public class AiConversaController {

    private final AiConversaService aiConversaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AiConversa create(@RequestParam String titulo) {
        return aiConversaService.create(titulo);
    }

    @GetMapping
    public List<AiConversa> listAll() {
        return aiConversaService.listAll();
    }

    @GetMapping("/{id}/mensagens")
    public List<AiMensagemResponseDTO> listMensagens(@PathVariable UUID id) {
        AiConversa conversa = aiConversaService.listAll()
                .stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Conversa não encontrada"));

        return aiConversaService.listMensagens(conversa.getId());

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        aiConversaService.delete(id);
    }
}
