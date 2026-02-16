package com.example.BoloDeLaMadre.services.iaService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.BoloDeLaMadre.dto.iaDto.AiMensagemResponseDTO;
import com.example.BoloDeLaMadre.entities.ia.AiConversa;
import com.example.BoloDeLaMadre.repositories.iaRepository.AiConversaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AiConversaService {

    private final AiConversaRepository aiConversaRepository;

    // Criar uma nova conversa
    public AiConversa create(String titulo) {
        AiConversa conv = new AiConversa();
        conv.setTitulo(titulo);

        aiConversaRepository.save(conv);
        return conv;
    }

    // Listar todas as conversas
    public List<AiConversa> listAll() {
        return aiConversaRepository.findAll();
    }

    // Listar todas as mensagens de uma conversa em DTO
    public List<AiMensagemResponseDTO> listMensagens(AiConversa conversa) {
        return conversa.getMensagens().stream()
                .map(AiMensagemResponseDTO::new)
                .collect(Collectors.toList());
    }

    // Deletar (soft delete)
    public void delete(java.util.UUID id) {
        AiConversa conv = aiConversaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conversa não encontrada"));
        // você pode fazer conv.setAtivo(false) se quiser soft delete
        aiConversaRepository.delete(conv);
    }
}
