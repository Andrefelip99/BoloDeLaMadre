package com.example.BoloDeLaMadre.services.iaService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.BoloDeLaMadre.dto.iaDto.AiMensagemRequestDTO;
import com.example.BoloDeLaMadre.dto.iaDto.AiMensagemResponseDTO;
import com.example.BoloDeLaMadre.entities.ia.AiConversa;
import com.example.BoloDeLaMadre.entities.ia.AiMensagem;
import com.example.BoloDeLaMadre.repositories.iaRepository.AiMensagemRepository;
import com.example.BoloDeLaMadre.repositories.iaRepository.AiConversaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AiMensagemService {

    private final AiMensagemRepository aiMensagemRepository;
    private final AiConversaRepository aiConversaRepository;

    public AiMensagemResponseDTO create(AiMensagemRequestDTO dto) {
        
        AiConversa conversa = aiConversaRepository.findById(dto.getConversaId())
                .orElseThrow(() -> new RuntimeException("Conversa não encontrada"));

        AiMensagem msg = new AiMensagem();
        msg.setConversa(conversa);
        msg.setRole("user"); 
        msg.setContent(dto.getContent());
       
        aiMensagemRepository.save(msg);

        return new AiMensagemResponseDTO(msg);
    }

    public List<AiMensagemResponseDTO> listByConversa(UUID conversaId) {
        AiConversa conversa = aiConversaRepository.findById(conversaId)
                .orElseThrow(() -> new RuntimeException("Conversa não encontrada"));

        return conversa.getMensagens().stream()
                .map(AiMensagemResponseDTO::new)
                .collect(Collectors.toList());
    }
}
