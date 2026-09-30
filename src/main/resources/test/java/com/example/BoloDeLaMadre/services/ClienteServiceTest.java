package com.example.BoloDeLaMadre.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.BoloDeLaMadre.entities.Cliente;
import com.example.BoloDeLaMadre.excepions.BadRequestException;
import com.example.BoloDeLaMadre.excepions.ResourceNotFoundException;
import com.example.BoloDeLaMadre.repositories.ClienteRepository;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {
    @Mock ClienteRepository repository;
    @InjectMocks ClienteService service;

    @Test
    void updateCopiesEditableFieldsToPersistedCustomer() {
        UUID id = UUID.randomUUID();
        Cliente stored = Cliente.builder().nome("Antigo").ativo(true).build();
        Cliente input = Cliente.builder().nome("Novo").email("novo@example.com").telefone("123").build();
        when(repository.findById(id)).thenReturn(Optional.of(stored));
        when(repository.save(any())).thenAnswer(call -> call.getArgument(0));

        Cliente result = service.update(id, input);

        assertThat(result.getNome()).isEqualTo("Novo");
        assertThat(result.getEmail()).isEqualTo("novo@example.com");
        assertThat(result.getTelefone()).isEqualTo("123");
        verify(repository).save(stored);
    }

    @Test
    void deleteSoftDeletesActiveCustomer() {
        UUID id = UUID.randomUUID();
        Cliente customer = Cliente.builder().ativo(true).build();
        when(repository.findById(id)).thenReturn(Optional.of(customer));

        service.delete(id);

        assertThat(customer.getAtivo()).isFalse();
        verify(repository).save(customer);
    }

    @Test
    void deleteRejectsInactiveOrMissingCustomer() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.of(Cliente.builder().ativo(false).build()));
        assertThatThrownBy(() -> service.delete(id)).isInstanceOf(BadRequestException.class)
                .hasMessage("Cliente já está inativo");
        verify(repository, never()).save(any());

        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.delete(id)).isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Cliente não encontrado");
    }
}
