package com.example.BoloDeLaMadre.controllers.iaController;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.BoloDeLaMadre.dto.iaDto.AiMensagemRequestDTO;
import com.example.BoloDeLaMadre.dto.iaDto.AiMensagemResponseDTO;
import com.example.BoloDeLaMadre.services.iaService.AiMensagemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ia/mensagens")
@RequiredArgsConstructor
public class AiMensagemController {

    private final AiMensagemService aiMensagemService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or @aiMensagemSecurity.isDonoDaConversa(#conversaId, authentication)")
    public ResponseEntity<AiMensagemResponseDTO> create(
            @RequestBody AiMensagemRequestDTO dto) {

        AiMensagemResponseDTO response = aiMensagemService.create(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/conversa/{conversaId}")
    @PreAuthorize("hasRole('FUNCIONARIO')")
    public ResponseEntity<List<AiMensagemResponseDTO>> listByConversa(
            @PathVariable UUID conversaId) {

        List<AiMensagemResponseDTO> list = aiMensagemService.listByConversa(conversaId);
        return ResponseEntity.ok(list);
    }
}
