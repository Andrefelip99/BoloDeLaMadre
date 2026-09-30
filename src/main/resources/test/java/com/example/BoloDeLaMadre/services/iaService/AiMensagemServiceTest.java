package com.example.BoloDeLaMadre.services.iaService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.BoloDeLaMadre.dto.iaDto.AiMensagemRequestDTO;
import com.example.BoloDeLaMadre.entities.ia.AiConversa;
import com.example.BoloDeLaMadre.entities.ia.AiMensagem;
import com.example.BoloDeLaMadre.excepions.BadRequestException;
import com.example.BoloDeLaMadre.excepions.ResourceNotFoundException;
import com.example.BoloDeLaMadre.repositories.iaRepository.AiConversaRepository;
import com.example.BoloDeLaMadre.repositories.iaRepository.AiMensagemRepository;

@ExtendWith(MockitoExtension.class)
class AiMensagemServiceTest {
    @Mock AiMensagemRepository messageRepository;
    @Mock AiConversaRepository conversationRepository;
    @Mock BusinessContextService businessContextService;
    @Mock OpenAiResponsesClient openAiResponsesClient;
    @InjectMocks AiMensagemService service;

    @Test
    void sendsQuestionAndContextToModelAndPersistsBothMessages() {
        UUID id = UUID.randomUUID();
        AiConversa conversation = AiConversa.builder().id(id).build();
        List<AiMensagem> saved = new ArrayList<>();
        when(conversationRepository.findById(id)).thenReturn(Optional.of(conversation));
        when(messageRepository.save(any())).thenAnswer(call -> {
            AiMensagem message = call.getArgument(0);
            saved.add(message);
            return message;
        });
        when(messageRepository.findByConversaIdWithConversa(id)).thenAnswer(call -> List.copyOf(saved));
        when(businessContextService.buildContext("Qual foi o faturamento?" )).thenReturn("Faturamento: R$ 100");
        when(openAiResponsesClient.respond(eq("Faturamento: R$ 100"), anyList())).thenReturn("O faturamento foi R$ 100.");

        var response = service.create(new AiMensagemRequestDTO(id, " Qual foi o faturamento? "));

        assertThat(response.getRole()).isEqualTo("assistant");
        assertThat(response.getContent()).isEqualTo("O faturamento foi R$ 100.");
        assertThat(saved).extracting(AiMensagem::getRole).containsExactly("user", "assistant");
        assertThat(saved).extracting(AiMensagem::getConversa).containsOnly(conversation);
        ArgumentCaptor<List<Map<String, String>>> history = ArgumentCaptor.forClass(List.class);
        verify(openAiResponsesClient).respond(eq("Faturamento: R$ 100"), history.capture());
        assertThat(history.getValue()).containsExactly(Map.of("role", "user", "content", "Qual foi o faturamento?"));
    }

    @Test
    void rejectsBlankMessageBeforeLookingUpConversation() {
        assertThatThrownBy(() -> service.create(new AiMensagemRequestDTO(UUID.randomUUID(), "  ")))
                .isInstanceOf(BadRequestException.class).hasMessage("A mensagem não pode estar vazia");
        verifyNoInteractions(conversationRepository, messageRepository, businessContextService, openAiResponsesClient);
    }

    @Test
    void reportsMissingConversationWithoutCallingModel() {
        UUID id = UUID.randomUUID();
        when(conversationRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.create(new AiMensagemRequestDTO(id, "Olá")))
                .isInstanceOf(ResourceNotFoundException.class).hasMessage("Conversa não encontrada");
        verifyNoInteractions(messageRepository, businessContextService, openAiResponsesClient);
    }
}
