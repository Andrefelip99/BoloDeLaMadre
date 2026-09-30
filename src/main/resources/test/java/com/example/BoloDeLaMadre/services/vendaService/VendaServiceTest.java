package com.example.BoloDeLaMadre.services.vendaService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.BoloDeLaMadre.dto.vendasDto.ItemVendaRequestDTO;
import com.example.BoloDeLaMadre.dto.vendasDto.VendaRequestDTO;
import com.example.BoloDeLaMadre.entities.Cliente;
import com.example.BoloDeLaMadre.entities.Funcionario;
import com.example.BoloDeLaMadre.entities.Produto;
import com.example.BoloDeLaMadre.entities.enums.StatusVenda;
import com.example.BoloDeLaMadre.entities.vendas.ItemVenda;
import com.example.BoloDeLaMadre.entities.vendas.Venda;
import com.example.BoloDeLaMadre.excepions.BadRequestException;
import com.example.BoloDeLaMadre.repositories.ClienteRepository;
import com.example.BoloDeLaMadre.repositories.FuncionarioRepository;
import com.example.BoloDeLaMadre.repositories.ProdutoRepository;
import com.example.BoloDeLaMadre.repositories.vendasRepository.ItemVendaRepository;
import com.example.BoloDeLaMadre.repositories.vendasRepository.VendaRepository;

@ExtendWith(MockitoExtension.class)
class VendaServiceTest {
    @Mock VendaRepository vendaRepository;
    @Mock ClienteRepository clienteRepository;
    @Mock FuncionarioRepository funcionarioRepository;
    @Mock ProdutoRepository produtoRepository;
    @Mock ItemVendaRepository itemVendaRepository;
    @InjectMocks VendaService service;

    @Test
    void createRejectsSaleWithoutItemsBeforeAccessingRepositories() {
        VendaRequestDTO request = new VendaRequestDTO();
        request.setItens(List.of());
        assertThatThrownBy(() -> service.create(request)).isInstanceOf(BadRequestException.class)
                .hasMessage("A venda deve possuir pelo menos um item");
        verifyNoInteractions(vendaRepository, clienteRepository, funcionarioRepository, produtoRepository, itemVendaRepository);
    }

    @SuppressWarnings("unchecked")
    @Test
    void createPersistsSaleAndComputesEachItemSubtotal() {
        UUID clienteId = UUID.randomUUID(), funcionarioId = UUID.randomUUID(), produtoId = UUID.randomUUID();
        Cliente cliente = Cliente.builder().nome("Ana").build();
        Funcionario funcionario = Funcionario.builder().nome("Bia").build();
        Produto produto = Produto.builder().nome("Bolo").build();
        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        when(funcionarioRepository.findById(funcionarioId)).thenReturn(Optional.of(funcionario));
        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));
        when(vendaRepository.save(any())).thenAnswer(call -> call.getArgument(0));
        when(itemVendaRepository.saveAll(any())).thenAnswer(call -> call.getArgument(0));
        ItemVendaRequestDTO item = new ItemVendaRequestDTO(null, produtoId, 2.0, new BigDecimal("12.50"), new BigDecimal("4.00"));
        VendaRequestDTO request = new VendaRequestDTO(clienteId, funcionarioId, null, List.of(item), BigDecimal.ZERO, BigDecimal.ZERO, null);

        var result = service.create(request);

        assertThat(result.getItens()).hasSize(1);
        assertThat(result.getItens().get(0).getSubtotal()).isEqualByComparingTo("25.00");
        ArgumentCaptor<Venda> saleCaptor = ArgumentCaptor.forClass(Venda.class);
        verify(vendaRepository).save(saleCaptor.capture());
        assertThat(saleCaptor.getValue().getCliente()).isSameAs(cliente);
        assertThat(saleCaptor.getValue().getFuncionario()).isSameAs(funcionario);
        assertThat(saleCaptor.getValue().getStatus()).isEqualTo(StatusVenda.PENDENTE);
        ArgumentCaptor<List<ItemVenda>> itemsCaptor = ArgumentCaptor.forClass(List.class);
        verify(itemVendaRepository).saveAll(itemsCaptor.capture());
        assertThat(itemsCaptor.getValue()).singleElement().satisfies(saved -> {
            assertThat(saved.getProduto()).isSameAs(produto);
            assertThat(saved.getSubtotal()).isEqualByComparingTo("25.00");
            assertThat(saved.getVenda()).isSameAs(saleCaptor.getValue());
        });
    }

    @Test
    void cancelMarksPendingSaleCancelledAndRejectsRepeatedCancellation() {
        UUID id = UUID.randomUUID();
        Venda sale = Venda.builder().status(StatusVenda.PENDENTE).build();
        when(vendaRepository.findById(id)).thenReturn(Optional.of(sale));
        service.cancelarVenda(id);
        assertThat(sale.getStatus()).isEqualTo(StatusVenda.CANCELADA);
        verify(vendaRepository).save(sale);

        when(vendaRepository.findById(id)).thenReturn(Optional.of(sale));
        assertThatThrownBy(() -> service.cancelarVenda(id)).isInstanceOf(BadRequestException.class)
                .hasMessage("Venda já está cancelada");
        verify(vendaRepository, times(1)).save(any());
    }
}
