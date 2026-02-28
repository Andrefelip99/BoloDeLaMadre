package com.example.BoloDeLaMadre.dto.iaDto;

import java.util.UUID;

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
    private String content;
}
