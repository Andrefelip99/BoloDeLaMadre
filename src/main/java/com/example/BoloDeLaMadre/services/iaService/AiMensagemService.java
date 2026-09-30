package com.example.BoloDeLaMadre.services.iaService;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.BoloDeLaMadre.dto.iaDto.AiMensagemRequestDTO;
import com.example.BoloDeLaMadre.dto.iaDto.AiMensagemResponseDTO;
import com.example.BoloDeLaMadre.entities.ia.AiConversa;
import com.example.BoloDeLaMadre.entities.ia.AiMensagem;
import com.example.BoloDeLaMadre.excepions.BadRequestException;
import com.example.BoloDeLaMadre.excepions.ResourceNotFoundException;
import com.example.BoloDeLaMadre.repositories.iaRepository.AiConversaRepository;
import com.example.BoloDeLaMadre.repositories.iaRepository.AiMensagemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AiMensagemService {
    private static final int MAX_MESSAGE_LENGTH = 4000;

    private final AiMensagemRepository aiMensagemRepository;
    private final AiConversaRepository aiConversaRepository;
    private final BusinessContextService businessContextService;

    public AiMensagemResponseDTO create(AiMensagemRequestDTO dto) {
        if (dto.getContent() == null || dto.getContent().isBlank()) {
            throw new BadRequestException("A mensagem não pode estar vazia");
        }
        if (dto.getContent().length() > MAX_MESSAGE_LENGTH) {
            throw new BadRequestException("A mensagem deve ter no máximo 4000 caracteres");
        }

        AiConversa conversa = aiConversaRepository.findById(dto.getConversaId())
                .orElseThrow(() -> new ResourceNotFoundException("Conversa não encontrada"));

        AiMensagem userMessage = new AiMensagem();
        userMessage.setConversa(conversa);
        userMessage.setRole("user");
        userMessage.setContent(dto.getContent().strip());
        userMessage.setCreatedAt(LocalDateTime.now());
        aiMensagemRepository.save(userMessage);

        String answer = businessContextService.buildContext(userMessage.getContent());

        AiMensagem assistantMessage = new AiMensagem();
        assistantMessage.setConversa(conversa);
        assistantMessage.setRole("assistant");
        assistantMessage.setContent(answer);
        assistantMessage.setCreatedAt(LocalDateTime.now());
        aiMensagemRepository.save(assistantMessage);

        return new AiMensagemResponseDTO(assistantMessage);
    }

    @SuppressWarnings("null")
public List<AiMensagemResponseDTO> listByConversa(UUID conversaId) {
        return aiMensagemRepository.findByConversaIdWithConversa(conversaId).stream()
                .sorted(Comparator.comparing(AiMensagem::getCreatedAt,
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .map(AiMensagemResponseDTO::new)
                .toList();
    }
}
