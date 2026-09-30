package com.example.BoloDeLaMadre.services.comprasEstoqueService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.BoloDeLaMadre.dto.comprasEstoqueDto.CompraRequestDTO;
import com.example.BoloDeLaMadre.dto.comprasEstoqueDto.CompraWithItemsDTO;
import com.example.BoloDeLaMadre.dto.comprasEstoqueDto.ItemCompraResponseDTO;
import com.example.BoloDeLaMadre.entities.Fornecedor;
import com.example.BoloDeLaMadre.entities.comprasEstoque.Compra;
import com.example.BoloDeLaMadre.excepions.ResourceNotFoundException;
import com.example.BoloDeLaMadre.repositories.FornecedorRepository;
import com.example.BoloDeLaMadre.repositories.comprasEstoqueRepository.CompraRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompraService {

    private final CompraRepository compraRepository;
    private final ItemCompraService itemCompraService;
    private final FornecedorRepository fornecedorRepository;

    @SuppressWarnings("null")
@Transactional
    public CompraWithItemsDTO create(CompraRequestDTO dto) {
        Fornecedor fornecedor = fornecedorRepository.findById(dto.getFornecedorId())
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado"));

        Compra compra = new Compra();
        compra.setFornecedor(fornecedor);
        compra.setDataCompra(dto.getDataCompra());
        compra.setTotal(BigDecimal.ZERO);
        compraRepository.save(compra);

        List<ItemCompraResponseDTO> itensDTO = dto.getItens().stream()
                .map(i -> itemCompraService.create(compra.getId(), i))
                .collect(Collectors.toList());

        BigDecimal total = itensDTO.stream()
                .map(ItemCompraResponseDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        compra.setTotal(total);
        compraRepository.save(compra);

        return new CompraWithItemsDTO(compra, itensDTO);
    }

    @Transactional(readOnly = true)
    public CompraWithItemsDTO getById(UUID id) {
        
        Compra compra = compraRepository.findByIdWithItens(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compra não encontrada"));

        List<ItemCompraResponseDTO> itensDTO = compra.getItens().stream()
                .map(ItemCompraResponseDTO::new)
                .collect(Collectors.toList());

        return new CompraWithItemsDTO(compra, itensDTO);
    }

    @Transactional(readOnly = true)
    public List<CompraWithItemsDTO> listAll() {
       
        return compraRepository.findAllWithItens().stream()
                .map(c -> new CompraWithItemsDTO(
                        c,
                        c.getItens().stream()
                         .map(ItemCompraResponseDTO::new)
                         .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }
}
