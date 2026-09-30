package com.example.BoloDeLaMadre.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.BoloDeLaMadre.entities.Ingrediente;
import com.example.BoloDeLaMadre.entities.enums.UnidadeMedida;
import com.example.BoloDeLaMadre.excepions.BadRequestException;
import com.example.BoloDeLaMadre.excepions.ResourceNotFoundException;
import com.example.BoloDeLaMadre.repositories.FornecedorRepository;
import com.example.BoloDeLaMadre.repositories.IngredienteRepository;

@ExtendWith(MockitoExtension.class)
class IngredienteServiceTest {
    @Mock IngredienteRepository repository;
    @Mock FornecedorRepository fornecedorRepository;
    @InjectMocks IngredienteService service;

    @Test
    void createAllowsNoSupplierAndInitializesIngredientAsActive() {
        when(repository.save(any())).thenAnswer(call -> call.getArgument(0));
        Ingrediente result = service.create("Farinha", UnidadeMedida.KG, new BigDecimal("4.25"), 3.0, 1.0, null);
        assertThat(result.getNome()).isEqualTo("Farinha");
        assertThat(result.getUnidade()).isEqualTo(UnidadeMedida.KG);
        assertThat(result.getAtivo()).isTrue();
        assertThat(result.getFornecedor()).isNull();
        verifyNoInteractions(fornecedorRepository);
    }

    @Test
    void updateRejectsInactiveIngredientWithoutSaving() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.of(Ingrediente.builder().ativo(false).build()));
        assertThatThrownBy(() -> service.update(id, "Farinha", UnidadeMedida.KG, BigDecimal.ONE, 1.0, 2.0, null))
                .isInstanceOf(BadRequestException.class).hasMessage("Ingrediente está inativo");
        verify(repository, never()).save(any());
        verifyNoInteractions(fornecedorRepository);
    }

    @Test
    void updateReportsMissingSupplierAndDoesNotPersist() {
        UUID id = UUID.randomUUID(), supplierId = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.of(Ingrediente.builder().ativo(true).build()));
        when(fornecedorRepository.findById(supplierId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.update(id, "Farinha", UnidadeMedida.KG, BigDecimal.ONE, 1.0, 2.0, supplierId))
                .isInstanceOf(ResourceNotFoundException.class).hasMessage("Fornecedor não encontrado");
        verify(repository, never()).save(any());
    }

    @Test
    void deleteMarksActiveIngredientInactive() {
        UUID id = UUID.randomUUID();
        Ingrediente ingredient = Ingrediente.builder().ativo(true).build();
        when(repository.findById(id)).thenReturn(Optional.of(ingredient));
        service.delete(id);
        assertThat(ingredient.getAtivo()).isFalse();
        verify(repository).save(ingredient);
    }
}
