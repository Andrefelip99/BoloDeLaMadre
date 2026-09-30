package com.example.BoloDeLaMadre.services.financeiroService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.BoloDeLaMadre.dto.financeiroDto.LancamentoFinanceiroRequestDTO;
import com.example.BoloDeLaMadre.entities.enums.TipoFinanceiro;
import com.example.BoloDeLaMadre.entities.financeiro.LancamentoFinanceiro;
import com.example.BoloDeLaMadre.repositories.financeiroRepository.LancamentoFinanceiroRepository;

@ExtendWith(MockitoExtension.class)
class LancamentoFinanceiroServiceTest {
    @Mock LancamentoFinanceiroRepository repository;
    @InjectMocks LancamentoFinanceiroService service;

    @Test
    void createPersistsUnpaidEntryFromRequest() {
        LancamentoFinanceiroRequestDTO request = new LancamentoFinanceiroRequestDTO();
        request.setTipo(TipoFinanceiro.RECEITA);
        request.setDescricao("Encomenda");
        request.setValor(new BigDecimal("125.00"));
        request.setDataLancamento(LocalDate.of(2025, 6, 4));
        when(repository.save(any())).thenAnswer(call -> call.getArgument(0));

        var response = service.create(request);

        ArgumentCaptor<LancamentoFinanceiro> captor = ArgumentCaptor.forClass(LancamentoFinanceiro.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getTipo()).isEqualTo(TipoFinanceiro.RECEITA);
        assertThat(captor.getValue().getValor()).isEqualByComparingTo("125.00");
        assertThat(captor.getValue().getPago()).isFalse();
        assertThat(response.getValor()).isEqualByComparingTo("125.00");
    }

    @Test
    void monthlySummaryUsesSelectedPeriodAndTreatsEmptySumsAsZero() {
        YearMonth period = YearMonth.of(2025, 6);
        when(repository.sumByTipoAndMesEAno(TipoFinanceiro.RECEITA, 6, 2025)).thenReturn(new BigDecimal("200.00"));
        when(repository.sumByTipoAndMesEAno(TipoFinanceiro.DESPESA, 6, 2025)).thenReturn(null);

        var summary = service.resumo(period);

        assertThat(summary.getTotalReceitas()).isEqualByComparingTo("200.00");
        assertThat(summary.getTotalDespesas()).isEqualByComparingTo("0");
        assertThat(summary.getLucro()).isEqualByComparingTo("200.00");
        verify(repository).sumByTipoAndMesEAno(TipoFinanceiro.RECEITA, 6, 2025);
        verify(repository).sumByTipoAndMesEAno(TipoFinanceiro.DESPESA, 6, 2025);
    }

    @Test
    void monthlyListPassesMonthAndYearToRepository() {
        when(repository.findByMesEAno(12, 2024)).thenReturn(List.of());
        assertThat(service.listByMesEAno(YearMonth.of(2024, 12))).isEmpty();
        verify(repository).findByMesEAno(12, 2024);
    }
}
