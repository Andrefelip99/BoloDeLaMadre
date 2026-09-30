package com.example.BoloDeLaMadre.dto.iaDto;

import java.util.UUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AiMensagemRequestDTO {

    private UUID conversaId;
    @NotBlank(message = "A mensagem não pode estar vazia")
    @Size(max = 4000, message = "A mensagem deve ter no máximo 4000 caracteres")
    private String content;
}
