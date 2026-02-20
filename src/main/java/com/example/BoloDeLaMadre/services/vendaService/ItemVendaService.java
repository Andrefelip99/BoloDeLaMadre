package com.example.BoloDeLaMadre.services.vendaService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.BoloDeLaMadre.dto.vendasDto.ItemVendaRequestDTO;
import com.example.BoloDeLaMadre.dto.vendasDto.ItemVendaResponseDTO;
import com.example.BoloDeLaMadre.entities.vendas.ItemVenda;
import com.example.BoloDeLaMadre.entities.vendas.Venda;
import com.example.BoloDeLaMadre.excepions.ResourceNotFoundException;
import com.example.BoloDeLaMadre.entities.Produto;
import com.example.BoloDeLaMadre.repositories.vendasRepository.ItemVendaRepository;
import com.example.BoloDeLaMadre.repositories.vendasRepository.VendaRepository;
import com.example.BoloDeLaMadre.repositories.ProdutoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemVendaService {

    private final ItemVendaRepository itemVendaRepository;
    private final VendaRepository vendaRepository;
    private final ProdutoRepository produtoRepository;

    public ItemVendaResponseDTO create(ItemVendaRequestDTO dto) {

        Venda venda = vendaRepository.findById(dto.getVendaId())
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada"));

        Produto produto = produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        ItemVenda item = new ItemVenda();
        item.setVenda(venda);
        item.setProduto(produto);
        item.setQuantidade(dto.getQuantidade());
        item.setPrecoUnitario(dto.getPrecoUnitario());
        item.setCustoUnitario(dto.getCustoUnitario());
        item.setSubtotal(item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())));


        itemVendaRepository.save(item);

        return new ItemVendaResponseDTO(item);
    }

    public List<ItemVendaResponseDTO> listByVenda(UUID vendaId) {
        return itemVendaRepository.findByVenda_Id(vendaId)
                .stream()
                .map(ItemVendaResponseDTO::new)
                .toList();
    }
}
