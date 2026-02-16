package com.example.BoloDeLaMadre.services.financeiroService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.BoloDeLaMadre.dto.financeiroDto.LancamentoFinanceiroRequestDTO;
import com.example.BoloDeLaMadre.dto.financeiroDto.LancamentoFinanceiroResponseDTO;
import com.example.BoloDeLaMadre.dto.financeiroDto.ResumoFinanceiroDTO;
import com.example.BoloDeLaMadre.entities.enums.TipoFinanceiro;
import com.example.BoloDeLaMadre.entities.financeiro.LancamentoFinanceiro;
import com.example.BoloDeLaMadre.repositories.financeiroRepository.LancamentoFinanceiroRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LancamentoFinanceiroService {

    private final LancamentoFinanceiroRepository lancamentoRepository;

    public LancamentoFinanceiroResponseDTO create(LancamentoFinanceiroRequestDTO dto) {
        LancamentoFinanceiro l = LancamentoFinanceiro.builder()
                .tipo(dto.getTipo())
                .categoria(dto.getCategoria())
                .descricao(dto.getDescricao())
                .valor(dto.getValor())
                .dataLancamento(dto.getDataLancamento())
                .pago(false) 
                .build();
        lancamentoRepository.save(l);
        return new LancamentoFinanceiroResponseDTO(l);
    }

    public List<LancamentoFinanceiroResponseDTO> listAll() {
        return lancamentoRepository.findAll().stream()
                .map(LancamentoFinanceiroResponseDTO::new)
                .collect(Collectors.toList());
    }

    public ResumoFinanceiroDTO resumo() {
    List<LancamentoFinanceiro> todos = lancamentoRepository.findAll();

    BigDecimal totalReceitas = todos.stream()
            .filter(l -> l.getTipo() == TipoFinanceiro.RECEITA)
            .map(LancamentoFinanceiro::getValor)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal totalDespesas = todos.stream()
            .filter(l -> l.getTipo() == TipoFinanceiro.DESPESA)
            .map(LancamentoFinanceiro::getValor)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal lucro = totalReceitas.subtract(totalDespesas);

    return new ResumoFinanceiroDTO(totalReceitas, totalDespesas, lucro);
}


}
