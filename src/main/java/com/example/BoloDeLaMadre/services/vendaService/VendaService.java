package com.example.BoloDeLaMadre.services.vendaService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.BoloDeLaMadre.dto.vendasDto.ItemVendaResponseDTO;
import com.example.BoloDeLaMadre.dto.vendasDto.VendaDetailsDTO;
import com.example.BoloDeLaMadre.dto.vendasDto.VendaRequestDTO;
import com.example.BoloDeLaMadre.entities.Cliente;
import com.example.BoloDeLaMadre.entities.Funcionario;
import com.example.BoloDeLaMadre.entities.Produto;
import com.example.BoloDeLaMadre.entities.enums.StatusVenda;
import com.example.BoloDeLaMadre.entities.vendas.ItemVenda;
import com.example.BoloDeLaMadre.entities.vendas.Venda;
import com.example.BoloDeLaMadre.excepions.BadRequestException;
import com.example.BoloDeLaMadre.excepions.ResourceNotFoundException;
import com.example.BoloDeLaMadre.repositories.ClienteRepository;
import com.example.BoloDeLaMadre.repositories.FuncionarioRepository;
import com.example.BoloDeLaMadre.repositories.ProdutoRepository;
import com.example.BoloDeLaMadre.repositories.vendasRepository.ItemVendaRepository;
import com.example.BoloDeLaMadre.repositories.vendasRepository.VendaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ClienteRepository clienteRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final ProdutoRepository produtoRepository;
    private final ItemVendaRepository itemVendaRepository;

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

        
        List<ItemVenda> itensVenda = dto.getItens().stream().map(itemDTO -> {
            Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

            BigDecimal subtotal = itemDTO.getPrecoUnitario().multiply(BigDecimal.valueOf(itemDTO.getQuantidade()));

            return ItemVenda.builder()
                    .venda(venda)
                    .produto(produto)
                    .quantidade(itemDTO.getQuantidade())
                    .precoUnitario(itemDTO.getPrecoUnitario())
                    .custoUnitario(itemDTO.getCustoUnitario())
                    .subtotal(subtotal)
                    .build();
        }).toList();

        itemVendaRepository.saveAll(itensVenda);

        
        List<ItemVendaResponseDTO> itensDTO = itensVenda.stream()
                .map(ItemVendaResponseDTO::new)
                .toList();

        return new VendaDetailsDTO(venda, itensDTO);
    }

    @Transactional(readOnly = true)
    public VendaDetailsDTO getById(UUID id) {
        Venda venda = vendaRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada"));

        List<ItemVendaResponseDTO> itensDTO = venda.getItens().stream()
                .map(ItemVendaResponseDTO::new)
                .toList();

        return new VendaDetailsDTO(venda, itensDTO);
    }

    @Transactional(readOnly = true)
    public List<VendaDetailsDTO> listAll() {
        return vendaRepository.findAllWithDetails().stream()
                .map(venda -> {
                    List<ItemVendaResponseDTO> itensDTO = venda.getItens().stream()
                            .map(ItemVendaResponseDTO::new)
                            .toList();
                    return new VendaDetailsDTO(venda, itensDTO);
                }).toList();
    }

    @Transactional
public void cancelarVenda(UUID id) {
    Venda venda = vendaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada"));

    if (venda.getStatus() == StatusVenda.CANCELADA) {
        throw new BadRequestException("Venda já está cancelada");
    }

    venda.setStatus(StatusVenda.CANCELADA);
    vendaRepository.save(venda);
}


}
