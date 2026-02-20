package com.example.BoloDeLaMadre.services.financeiroService;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
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

    public List<LancamentoFinanceiroResponseDTO> listByMesEAno(YearMonth periodo) {
        return lancamentoRepository
                .findByMesEAno(periodo.getMonthValue(), periodo.getYear())
                .stream()
                .map(LancamentoFinanceiroResponseDTO::new)
                .collect(Collectors.toList());
    }

    public ResumoFinanceiroDTO resumo(YearMonth periodo) {
        int mes = periodo.getMonthValue();
        int ano = periodo.getYear();

        BigDecimal totalReceitas = 
                lancamentoRepository.sumByTipoAndMesEAno(TipoFinanceiro.RECEITA, mes, ano);
        BigDecimal totalDespesas = 
                lancamentoRepository.sumByTipoAndMesEAno(TipoFinanceiro.DESPESA, mes, ano);

        totalReceitas = totalReceitas != null ? totalReceitas : BigDecimal.ZERO;
        totalDespesas = totalDespesas != null ? totalDespesas : BigDecimal.ZERO;

        return new ResumoFinanceiroDTO(totalReceitas, totalDespesas, totalReceitas.subtract(totalDespesas));
    }
}
