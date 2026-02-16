package com.example.BoloDeLaMadre.dto.iaDto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.BoloDeLaMadre.entities.ia.AiMensagem;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AiMensagemResponseDTO {

    private UUID id;
    private String role;
    private String content;
    private LocalDateTime createdAt;

    public AiMensagemResponseDTO(AiMensagem entity){
        this.id = entity.getId();
        this.role = entity.getRole();
        this.content = entity.getContent();
        this.createdAt = entity.getCreatedAt();
    }
}

