package com.example.BoloDeLaMadre.services.relatorioService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.BoloDeLaMadre.dto.relatorioDto.AiInsightDTO;
import com.example.BoloDeLaMadre.dto.relatorioDto.DreDataDTO;
import com.example.BoloDeLaMadre.dto.relatorioDto.KpiDataDTO;
import com.example.BoloDeLaMadre.entities.enums.TipoFinanceiro;
import com.example.BoloDeLaMadre.entities.financeiro.LancamentoFinanceiro;
import com.example.BoloDeLaMadre.entities.vendas.Venda;
import com.example.BoloDeLaMadre.repositories.IngredienteRepository;
import com.example.BoloDeLaMadre.repositories.financeiroRepository.LancamentoFinanceiroRepository;
import com.example.BoloDeLaMadre.repositories.vendasRepository.VendaRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class RelatorioService {

    private final VendaRepository vendaRepository;
    private final LancamentoFinanceiroRepository lancamentoRepository;
    private final IngredienteRepository ingredienteRepository;

    

    public DreDataDTO gerarDre(YearMonth periodo) {

        List<LancamentoFinanceiro> lancamentos = lancamentoRepository.findAll();

        BigDecimal receitas = somarPorTipo(lancamentos, TipoFinanceiro.RECEITA);
        BigDecimal despesas = somarPorTipo(lancamentos, TipoFinanceiro.DESPESA);

        DreDataDTO dto = new DreDataDTO();
        dto.setReceitaBruta(receitas);
        dto.setResultadoLiquido(receitas.subtract(despesas));

        return dto;
    }

    private BigDecimal somarPorTipo(List<LancamentoFinanceiro> lancamentos, TipoFinanceiro tipo) {
        return lancamentos.stream()
                .filter(l -> l.getTipo() == tipo)
                .map(LancamentoFinanceiro::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

   

    public KpiDataDTO gerarKpi(YearMonth mesAtual) {

        YearMonth mesAnterior = mesAtual.minusMonths(1);
        var vendas = vendaRepository.findAll();

        BigDecimal receitaMes = somarVendasPorMes(vendas, mesAtual);
        BigDecimal receitaMesAnterior = somarVendasPorMes(vendas, mesAnterior);

        long quantidadeVendas = vendas.stream()
                .filter(v -> YearMonth.from(v.getDataVenda()).equals(mesAtual))
                .count();

        BigDecimal ticketMedio = quantidadeVendas == 0
                ? BigDecimal.ZERO
                : receitaMes.divide(BigDecimal.valueOf(quantidadeVendas), 2, RoundingMode.HALF_UP);

        long estoqueBaixo = ingredienteRepository.countByEstoqueAtualLessThanEstoqueMinimo();

        KpiDataDTO dto = new KpiDataDTO();
        dto.setReceitaMes(receitaMes);
        dto.setReceitaMesAnterior(receitaMesAnterior);
        dto.setQuantidadeVendas(quantidadeVendas);
        dto.setTicketMedio(ticketMedio);
        dto.setItensEstoqueBaixo(estoqueBaixo);

        return dto;
    }

    private BigDecimal somarVendasPorMes(List<Venda> vendas, YearMonth mes) {
        return vendas.stream()
                .filter(v -> YearMonth.from(v.getDataVenda()).equals(mes))
                .map(Venda::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    public List<AiInsightDTO> gerarInsights(YearMonth mes) {

        KpiDataDTO kpi = gerarKpi(mes);
        List<AiInsightDTO> insights = new ArrayList<>();

        addInsight(
                insights,
                kpi.getReceitaMes().compareTo(kpi.getReceitaMesAnterior()) > 0,
                AiInsightDTO.TipoInsight.POSITIVO,
                "Crescimento nas vendas",
                "Sua receita aumentou em relação ao mês passado",
                null
        );

        addInsight(
                insights,
                kpi.getItensEstoqueBaixo() > 0,
                AiInsightDTO.TipoInsight.ALERTA,
                "Estoque baixo",
                "Existem ingredientes abaixo do estoque mínimo",
                "Realizar reposição"
        );

        addInsight(
                insights,
                kpi.getTicketMedio().compareTo(BigDecimal.valueOf(50)) < 0,
                AiInsightDTO.TipoInsight.OPORTUNIDADE,
                "Aumentar ticket médio",
                "Crie combos para aumentar o valor por venda",
                null
        );

        return insights;
    }

    private void addInsight(List<AiInsightDTO> lista,
                            boolean condicao,
                            AiInsightDTO.TipoInsight tipo,
                            String titulo,
                            String descricao,
                            String acao) {

        if (!condicao) return;

        AiInsightDTO dto = new AiInsightDTO();
        dto.setTipo(tipo);
        dto.setTitulo(titulo);
        dto.setDescricao(descricao);
        dto.setAcao(acao);

        lista.add(dto);
    }
}
