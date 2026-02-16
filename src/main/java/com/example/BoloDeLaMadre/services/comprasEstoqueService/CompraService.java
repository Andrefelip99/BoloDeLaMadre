package com.example.BoloDeLaMadre.services.comprasEstoqueService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.BoloDeLaMadre.dto.comprasEstoqueDto.CompraRequestDTO;
import com.example.BoloDeLaMadre.dto.comprasEstoqueDto.CompraWithItemsDTO;
import com.example.BoloDeLaMadre.dto.comprasEstoqueDto.ItemCompraResponseDTO;
import com.example.BoloDeLaMadre.entities.Fornecedor;
import com.example.BoloDeLaMadre.entities.comprasEstoque.Compra;
import com.example.BoloDeLaMadre.repositories.FornecedorRepository;
import com.example.BoloDeLaMadre.repositories.comprasEstoqueRepository.CompraRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompraService {

    private final CompraRepository compraRepository;
    private final ItemCompraService itemCompraService;
    private final FornecedorRepository fornecedorRepository;

    public CompraWithItemsDTO create(CompraRequestDTO dto) {

        // 1️⃣ Busca fornecedor
        Fornecedor fornecedor = fornecedorRepository.findById(dto.getFornecedorId())
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

        // 2️⃣ Cria a compra
        Compra compra = new Compra();
        compra.setFornecedor(fornecedor);

        // DTO já é LocalDateTime, seta direto
        compra.setDataCompra(dto.getDataCompra());

        compra.setTotal(BigDecimal.ZERO);
        compraRepository.save(compra);

        // 3️⃣ Cria itens e recebe DTOs de resposta
        List<ItemCompraResponseDTO> itensDTO = dto.getItens().stream()
                .map(i -> itemCompraService.create(compra.getId(), i)) // retorna ItemCompraResponseDTO
                .collect(Collectors.toList());

        // 4️⃣ Calcula total da compra
        BigDecimal total = itensDTO.stream()
                .map(ItemCompraResponseDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        compra.setTotal(total);
        compraRepository.save(compra);

        // 5️⃣ Retorna DTO final
        return new CompraWithItemsDTO(compra, itensDTO);
    }

    public CompraWithItemsDTO getById(UUID id) {
        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra não encontrada"));

        List<ItemCompraResponseDTO> itensDTO = itemCompraService.listByCompra(compra.getId());

        return new CompraWithItemsDTO(compra, itensDTO);
    }

    public List<CompraWithItemsDTO> listAll() {
        return compraRepository.findAll().stream()
                .map(c -> new CompraWithItemsDTO(c, itemCompraService.listByCompra(c.getId())))
                .collect(Collectors.toList());
    }
}
