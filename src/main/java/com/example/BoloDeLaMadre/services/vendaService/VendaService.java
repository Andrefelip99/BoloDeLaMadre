package com.example.BoloDeLaMadre.services.vendaService;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.BoloDeLaMadre.dto.vendasDto.ItemVendaResponseDTO;
import com.example.BoloDeLaMadre.dto.vendasDto.VendaDetailsDTO;
import com.example.BoloDeLaMadre.dto.vendasDto.VendaRequestDTO;
import com.example.BoloDeLaMadre.entities.Cliente;
import com.example.BoloDeLaMadre.entities.Funcionario;
import com.example.BoloDeLaMadre.entities.enums.StatusVenda;
import com.example.BoloDeLaMadre.entities.vendas.Venda;
import com.example.BoloDeLaMadre.excepions.ResourceNotFoundException;
import com.example.BoloDeLaMadre.repositories.ClienteRepository;
import com.example.BoloDeLaMadre.repositories.FuncionarioRepository;
import com.example.BoloDeLaMadre.repositories.vendasRepository.VendaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VendaService {

        private final VendaRepository vendaRepository;
        private final ClienteRepository clienteRepository;
        private final FuncionarioRepository funcionarioRepository;

        @Transactional
        public VendaDetailsDTO create(VendaRequestDTO dto) {
                Cliente cliente = clienteRepository.findById(dto.getClienteId())
                                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

                Funcionario funcionario = funcionarioRepository.findById(dto.getFuncionarioId())
                                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado"));

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
                Venda venda = vendaRepository.findByIdWithDetails(id) 
                                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada"));

                
                List<ItemVendaResponseDTO> itensDTO = venda.getItens().stream()
                                .map(ItemVendaResponseDTO::new)
                                .toList();

                return new VendaDetailsDTO(venda, itensDTO);
        }

        public List<VendaDetailsDTO> listAll() {
                return vendaRepository.findAllWithDetails() 
                                .stream()
                                .map(v -> new VendaDetailsDTO(
                                                v,
                                                v.getItens().stream()
                                                                .map(ItemVendaResponseDTO::new)
                                                                .toList()))
                                .toList();
        }
}
