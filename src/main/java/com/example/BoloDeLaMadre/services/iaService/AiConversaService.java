package com.example.BoloDeLaMadre.services.iaService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.example.BoloDeLaMadre.dto.iaDto.AiMensagemResponseDTO;
import com.example.BoloDeLaMadre.entities.Funcionario;
import com.example.BoloDeLaMadre.entities.ia.AiConversa;
import com.example.BoloDeLaMadre.excepions.ResourceNotFoundException;
import com.example.BoloDeLaMadre.repositories.FuncionarioRepository;
import com.example.BoloDeLaMadre.repositories.iaRepository.AiConversaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AiConversaService {

    private final AiConversaRepository repository;
    private final FuncionarioRepository funcionarioRepository;

    private UUID getFuncionarioId() {
        return UUID.fromString(
                SecurityContextHolder.getContext().getAuthentication().getName());
    }

    private boolean isAdmin() {
        return SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    public AiConversa create(String titulo) {

        Funcionario funcionario = funcionarioRepository.findById(getFuncionarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado"));

        AiConversa conversa = AiConversa.builder()
                .titulo(titulo)
                .funcionario(funcionario)
                .createdAt(LocalDateTime.now())
                .build();

        return repository.save(conversa);
    }

    public List<AiConversa> list() {

        if (isAdmin()) {
            return repository.findAllWithMensagens();
        }

        return repository.findByFuncionarioId(getFuncionarioId());
    }

    public List<AiMensagemResponseDTO> listMensagens(UUID conversaId) {

        AiConversa conversa = repository.findByIdWithMensagens(conversaId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversa não encontrada"));

        if (!isAdmin() &&
                !conversa.getFuncionario().getId().equals(getFuncionarioId())) {

            throw new AccessDeniedException("Acesso negado");
        }

        return conversa.getMensagens()
                .stream()
                .map(AiMensagemResponseDTO::new)
                .toList();
    }

    public void delete(UUID conversaId) {

        AiConversa conversa = repository.findById(conversaId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversa não encontrada"));

        if (!isAdmin() &&
                !conversa.getFuncionario().getId().equals(getFuncionarioId())) {

            throw new AccessDeniedException("Acesso negado");
        }

        repository.delete(conversa);
    }
}
