package com.example.BoloDeLaMadre.services.iaService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.example.BoloDeLaMadre.dto.iaDto.AiMensagemResponseDTO;
import com.example.BoloDeLaMadre.entities.ia.AiConversa;
import com.example.BoloDeLaMadre.excepions.ResourceNotFoundException;
import com.example.BoloDeLaMadre.repositories.iaRepository.AiConversaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AiConversaService {

    private final AiConversaRepository repository;

    public AiConversa create(String titulo) {
        AiConversa conversa = AiConversa.builder()
                .titulo(titulo)
                .createdAt(LocalDateTime.now())
                .build();
        return repository.save(conversa);
    }

    public List<AiConversa> list() {
        return repository.findAllWithMensagens();
    }

    public List<AiMensagemResponseDTO> listMensagens(UUID conversaId) {

        AiConversa conversa = repository.findByIdWithMensagens(conversaId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversa não encontrada"));

        return conversa.getMensagens()
                .stream()
                .map(AiMensagemResponseDTO::new)
                .toList();
    }

    public void delete(UUID conversaId) {

        AiConversa conversa = repository.findById(conversaId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversa não encontrada"));

        repository.delete(conversa);
    }
}
