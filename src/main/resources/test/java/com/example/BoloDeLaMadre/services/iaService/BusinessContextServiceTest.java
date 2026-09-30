package com.example.BoloDeLaMadre.services.iaService;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.BoloDeLaMadre.entities.Ingrediente;
import com.example.BoloDeLaMadre.entities.Produto;
import com.example.BoloDeLaMadre.entities.Receita;
import com.example.BoloDeLaMadre.entities.enums.StatusVenda;
import com.example.BoloDeLaMadre.entities.enums.UnidadeMedida;
import com.example.BoloDeLaMadre.entities.vendas.Venda;
import com.example.BoloDeLaMadre.entities.vendas.ItemVenda;
import com.example.BoloDeLaMadre.repositories.IngredienteRepository;
import com.example.BoloDeLaMadre.repositories.ProdutoRepository;
import com.example.BoloDeLaMadre.repositories.ReceitaRepository;
import com.example.BoloDeLaMadre.repositories.vendasRepository.VendaRepository;

@ExtendWith(MockitoExtension.class)
class BusinessContextServiceTest {
    @Mock VendaRepository vendaRepository;
    @Mock IngredienteRepository ingredienteRepository;
    @Mock ProdutoRepository produtoRepository;
    @Mock ReceitaRepository receitaRepository;
    @InjectMocks BusinessContextService service;

    @Test
    void salesContextUsesRequestedMonthAndOmitsCancelledSales() {
        Venda completed = Venda.builder().status(StatusVenda.ENTREGUE).total(new BigDecimal("125.00")).build();
        Venda pending = Venda.builder().status(StatusVenda.PENDENTE).total(new BigDecimal("75.00")).build();
        Venda cancelled = Venda.builder().status(StatusVenda.CANCELADA).total(new BigDecimal("999.00")).build();
        when(vendaRepository.findByMesEAno(4, 2025)).thenReturn(List.of(completed, pending, cancelled));

        String context = service.buildContext("Resuma as vendas em 2025-04");

        assertThat(context).contains("04/2025", "2 venda(s)", "R$ 200.00", "R$ 100.00");
        assertThat(context).doesNotContain("999.00");
        verify(vendaRepository).findByMesEAno(4, 2025);
    }

    @Test
    void salesContextFallsBackToItemSubtotalsWhenSaleTotalWasNotRecorded() {
        Venda sale = Venda.builder().status(StatusVenda.ENTREGUE).itens(List.of(
                ItemVenda.builder().subtotal(new BigDecimal("42.50")).build())).build();
        when(vendaRepository.findByMesEAno(4, 2025)).thenReturn(List.of(sale));
        when(vendaRepository.findByMesEAno(3, 2025)).thenReturn(List.of());

        String context = service.buildContext("desempenho em 2025-04");

        assertThat(context).contains("R$ 42.50", "não considera descontos nem taxas");
    }

    @Test
    void stockContextOnlyIncludesActiveIngredientsAndTheirThresholds() {
        when(ingredienteRepository.findAll()).thenReturn(List.of(
                ingredient("Farinha", 2.0, 5.0, true), ingredient("Inativo", 0.0, 5.0, false)));

        String context = service.buildContext("Como está o estoque de ingredientes?");

        assertThat(context).contains("Farinha", "abaixo do mínimo").doesNotContain("Inativo");
        verifyNoInteractions(vendaRepository, produtoRepository, receitaRepository);
    }

    @Test
    void productionEstimateScalesRecipeQuantitiesAndReportsUnits() {
        Produto product = Produto.builder().nome("Bolo de Cenoura").ativo(true).build();
        Ingrediente carrot = ingredient("Cenoura", 1.5, 0.5, true);
        when(produtoRepository.findAll()).thenReturn(List.of(product));
        when(receitaRepository.findAllWithProdutoAndIngrediente()).thenReturn(List.of(
                Receita.builder().produto(product).ingrediente(carrot).quantidade(0.25).unidade(UnidadeMedida.KG).ativo(true).build()));

        String context = service.buildContext("estimar ingredientes para 12 unidades de Bolo de Cenoura");

        assertThat(context).contains("12.000", "Bolo de Cenoura", "3.000 KG", "estoque atual 1.500 KG");
        verifyNoInteractions(vendaRepository, ingredienteRepository);
    }

    @Test
    void productionEstimateAsksForProductAndQuantityWhenInformationIsMissing() {
        when(produtoRepository.findAll()).thenReturn(List.of(Produto.builder().nome("Bolo de Cenoura").ativo(true).build()));
        when(receitaRepository.findAllWithProdutoAndIngrediente()).thenReturn(List.of());

        String context = service.buildContext("Quero estimar ingredientes");

        assertThat(context).contains("nome de um produto", "quantidade", "Bolo de Cenoura");
    }

    private Ingrediente ingredient(String name, double current, double min, boolean active) {
        return Ingrediente.builder().nome(name).unidade(UnidadeMedida.KG).custoUnitario(BigDecimal.ONE)
                .estoqueAtual(current).estoqueMinimo(min).ativo(active).build();
    }
}
