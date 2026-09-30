package com.example.BoloDeLaMadre.repositories;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.example.BoloDeLaMadre.entities.Categoria;
import com.example.BoloDeLaMadre.entities.Fornecedor;
import com.example.BoloDeLaMadre.entities.Ingrediente;
import com.example.BoloDeLaMadre.entities.Produto;
import com.example.BoloDeLaMadre.entities.enums.TipoFinanceiro;
import com.example.BoloDeLaMadre.entities.enums.UnidadeMedida;
import com.example.BoloDeLaMadre.entities.financeiro.LancamentoFinanceiro;
import com.example.BoloDeLaMadre.repositories.financeiroRepository.LancamentoFinanceiroRepository;

@DataJpaTest
class ConsultasRepositoryTest {
    @Autowired ProdutoRepository produtoRepository;
    @Autowired CategoriaRepository categoriaRepository;
    @Autowired IngredienteRepository ingredienteRepository;
    @Autowired FornecedorRepository fornecedorRepository;
    @Autowired LancamentoFinanceiroRepository financeiroRepository;

    @SuppressWarnings("null")
    @Test
    void produtoFetchQueriesReturnProductsWithTheirCategory() {
        Categoria category = categoriaRepository.save(Categoria.builder().nome("Tortas").ativo(true).build());
        Produto product = produtoRepository.save(Produto.builder().nome("Torta de limão").preco(new BigDecimal("30.00")).categoria(category).ativo(true).build());
        produtoRepository.flush();

        assertThat(produtoRepository.findByIdWithCategoria(product.getId())).get()
                .satisfies(found -> assertThat(found.getCategoria().getNome()).isEqualTo("Tortas"));
        assertThat(produtoRepository.findAllWithCategoria()).extracting(Produto::getNome).containsExactly("Torta de limão");
        assertThat(produtoRepository.findByIdWithCategoria(UUID.randomUUID())).isEmpty();
    }

    @SuppressWarnings("null")
    @Test
    void ingredientFetchAndLowStockQueriesHonorActiveAndThresholdFilters() {
        Fornecedor supplier = fornecedorRepository.save(Fornecedor.builder().nome("Fornecedor").ativo(true).build());
        Ingrediente low = ingredienteRepository.save(ingredient("Farinha", 2.0, 5.0, true, supplier));
        ingredienteRepository.save(ingredient("Açúcar", 5.0, 5.0, true, supplier));
        ingredienteRepository.save(ingredient("Inativo", 1.0, 5.0, false, supplier));
        ingredienteRepository.flush();

        assertThat(ingredienteRepository.countByEstoqueAbaixoDoMinimo()).isEqualTo(1);
        assertThat(ingredienteRepository.findByIdWithFornecedor(low.getId())).get()
                .satisfies(found -> assertThat(found.getFornecedor().getNome()).isEqualTo("Fornecedor"));
        assertThat(ingredienteRepository.findAllWithFornecedor()).extracting(Ingrediente::getNome)
                .containsExactlyInAnyOrder("Farinha", "Açúcar");
        assertThat(ingredienteRepository.findByIdWithFornecedor(UUID.randomUUID())).isEmpty();
    }

    @Test
    void financialMonthQueriesFilterYearMonthAndSumByType() {
        financeiroRepository.save(entry(TipoFinanceiro.RECEITA, "100.00", LocalDate.of(2025, 4, 2)));
        financeiroRepository.save(entry(TipoFinanceiro.RECEITA, "25.50", LocalDate.of(2025, 4, 28)));
        financeiroRepository.save(entry(TipoFinanceiro.DESPESA, "30.00", LocalDate.of(2025, 4, 14)));
        financeiroRepository.save(entry(TipoFinanceiro.RECEITA, "999.00", LocalDate.of(2025, 5, 1)));
        financeiroRepository.flush();

        assertThat(financeiroRepository.findByMesEAno(4, 2025)).hasSize(3);
        assertThat(financeiroRepository.sumByTipoAndMesEAno(TipoFinanceiro.RECEITA, 4, 2025))
                .isEqualByComparingTo("125.50");
        assertThat(financeiroRepository.sumByTipoAndMesEAno(TipoFinanceiro.DESPESA, 4, 2025))
                .isEqualByComparingTo("30.00");
        assertThat(financeiroRepository.findByMesEAno(4, 2024)).isEmpty();
    }

    private Ingrediente ingredient(String name, double current, double min, boolean active, Fornecedor supplier) {
        return Ingrediente.builder().nome(name).unidade(UnidadeMedida.KG).custoUnitario(BigDecimal.ONE)
                .estoqueAtual(current).estoqueMinimo(min).ativo(active).fornecedor(supplier).build();
    }

    private LancamentoFinanceiro entry(TipoFinanceiro type, String value, LocalDate date) {
        return LancamentoFinanceiro.builder().tipo(type).valor(new BigDecimal(value)).dataLancamento(date).pago(false).build();
    }
}
