package com.example.BoloDeLaMadre.services.iaService;

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

    private final AiConversaRepository aiConversaRepository;

    
    public AiConversa create(String titulo) {
        AiConversa conv = AiConversa.builder()
                .titulo(titulo)
                .ativo(true)
                .build();
        return aiConversaRepository.save(conv);
    }

    public List<AiConversa> listAll() {
        return aiConversaRepository.findAllWithMensagens();
    }

    
    public List<AiMensagemResponseDTO> listMensagens(UUID conversaId) {
        AiConversa conversa = aiConversaRepository.findByIdWithMensagens(conversaId);
        if (conversa == null) throw new ResourceNotFoundException("Conversa não encontrada");

        return conversa.getMensagens().stream()
                .map(AiMensagemResponseDTO::new)
                .toList();
    }

   
    public void delete(UUID id) {
        AiConversa conv = aiConversaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conversa não encontrada"));
        aiConversaRepository.delete(conv);
    }
}
