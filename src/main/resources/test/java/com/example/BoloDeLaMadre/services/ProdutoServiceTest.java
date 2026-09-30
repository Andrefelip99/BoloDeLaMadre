package com.example.BoloDeLaMadre.services;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.BoloDeLaMadre.dto.ProdutoRequestDTO;
import com.example.BoloDeLaMadre.entities.Categoria;
import com.example.BoloDeLaMadre.entities.Produto;
import com.example.BoloDeLaMadre.excepions.BadRequestException;
import com.example.BoloDeLaMadre.excepions.ResourceNotFoundException;
import com.example.BoloDeLaMadre.repositories.CategoriaRepository;
import com.example.BoloDeLaMadre.repositories.ProdutoRepository;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {
    @Mock ProdutoRepository produtoRepository;
    @Mock CategoriaRepository categoriaRepository;
    @InjectMocks ProdutoService service;

    @Test
    void createDefaultsToActiveAndResolvesOptionalCategory() {
        UUID categoryId = UUID.randomUUID();
        Categoria category = Categoria.builder().nome("Bolos").ativo(true).build();
        ProdutoRequestDTO dto = request("Bolo", categoryId);
        when(categoriaRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(produtoRepository.save(any())).thenAnswer(call -> call.getArgument(0));

        Produto result = service.create(dto);

        assertThat(result.getNome()).isEqualTo("Bolo");
        assertThat(result.getPreco()).isEqualByComparingTo("25.50");
        assertThat(result.getAtivo()).isTrue();
        assertThat(result.getCategoria()).isSameAs(category);
        verify(produtoRepository).save(result);
    }

    @Test
    void createWithoutCategoryDoesNotQueryCategoryRepository() {
        when(produtoRepository.save(any())).thenAnswer(call -> call.getArgument(0));
        Produto result = service.create(request("Bolo", null));
        assertThat(result.getCategoria()).isNull();
        verifyNoInteractions(categoriaRepository);
    }

    @Test
    void createRejectsMissingCategory() {
        UUID categoryId = UUID.randomUUID();
        when(categoriaRepository.findById(categoryId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.create(request("Bolo", categoryId)))
                .isInstanceOf(ResourceNotFoundException.class).hasMessage("Categoria não encontrada");
        verifyNoInteractions(produtoRepository);
    }

    @Test
    void updateReplacesFieldsAndAllowsRemovingCategory() {
        UUID id = UUID.randomUUID();
        Produto existing = Produto.builder().nome("Antigo").preco(BigDecimal.ONE).ativo(true).build();
        when(produtoRepository.findById(id)).thenReturn(Optional.of(existing));
        when(produtoRepository.save(any())).thenAnswer(call -> call.getArgument(0));

        Produto updated = service.update(id, request("Novo", null));

        assertThat(updated.getNome()).isEqualTo("Novo");
        assertThat(updated.getPreco()).isEqualByComparingTo("25.50");
        assertThat(updated.getCategoria()).isNull();
        verify(produtoRepository).save(existing);
    }

    @Test
    void deleteSoftDeletesActiveProduct() {
        UUID id = UUID.randomUUID();
        Produto product = Produto.builder().ativo(true).build();
        when(produtoRepository.findById(id)).thenReturn(Optional.of(product));
        service.delete(id);
        assertThat(product.getAtivo()).isFalse();
        verify(produtoRepository).save(product);
    }

    @Test
    void deleteRejectsAlreadyInactiveProduct() {
        UUID id = UUID.randomUUID();
        when(produtoRepository.findById(id)).thenReturn(Optional.of(Produto.builder().ativo(false).build()));
        assertThatThrownBy(() -> service.delete(id)).isInstanceOf(BadRequestException.class)
                .hasMessage("Produto já está inativo");
        verify(produtoRepository, never()).save(any());
    }

    @Test
    void getByIdReportsMissingProduct() {
        UUID id = UUID.randomUUID();
        when(produtoRepository.findByIdWithCategoria(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getById(id)).isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Produto não encontrado");
    }

    private ProdutoRequestDTO request(String name, UUID categoryId) {
        ProdutoRequestDTO dto = new ProdutoRequestDTO();
        dto.setNome(name);
        dto.setPreco(new BigDecimal("25.50"));
        dto.setCategoriaId(categoryId);
        return dto;
    }
}
