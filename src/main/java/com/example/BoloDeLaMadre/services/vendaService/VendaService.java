package com.example.BoloDeLaMadre.services.vendaService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.BoloDeLaMadre.dto.vendasDto.ItemVendaResponseDTO;
import com.example.BoloDeLaMadre.dto.vendasDto.VendaDetailsDTO;
import com.example.BoloDeLaMadre.dto.vendasDto.VendaRequestDTO;
import com.example.BoloDeLaMadre.entities.Cliente;
import com.example.BoloDeLaMadre.entities.Funcionario;
import com.example.BoloDeLaMadre.entities.enums.StatusVenda;
import com.example.BoloDeLaMadre.entities.vendas.Venda;
import com.example.BoloDeLaMadre.repositories.ClienteRepository;
import com.example.BoloDeLaMadre.repositories.FuncionarioRepository;
import com.example.BoloDeLaMadre.repositories.vendasRepository.VendaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ItemVendaService itemVendaService;

    private final ClienteRepository clienteRepository;
    private final FuncionarioRepository funcionarioRepository;

    @Transactional
    public VendaDetailsDTO create(VendaRequestDTO dto) {

    Cliente cliente = clienteRepository.findById(dto.getClienteId())
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

    Funcionario funcionario = funcionarioRepository.findById(dto.getFuncionarioId())
            .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));

    Venda venda = Venda.builder()
            .cliente(cliente)
            .funcionario(funcionario)
            .canal(dto.getCanal())
            .status(StatusVenda.PENDENTE)
            .desconto(dto.getDesconto())
            .taxaEntrega(dto.getTaxaEntrega())
            .formaPagamento(dto.getFormaPagamento())
            .build();

    vendaRepository.save(venda);

    return new VendaDetailsDTO(venda, List.of());
}


    public VendaDetailsDTO getById(UUID id) {

        Venda venda = vendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada"));

        List<ItemVendaResponseDTO> itens =
                itemVendaService.listByVenda(venda.getId());

        return new VendaDetailsDTO(venda, itens);
    }

    public List<VendaDetailsDTO> listAll() {

        return vendaRepository.findAll()
                .stream()
                .map(v -> new VendaDetailsDTO(
                        v,
                        itemVendaService.listByVenda(v.getId())
                ))
                .collect(Collectors.toList());
    }
}
