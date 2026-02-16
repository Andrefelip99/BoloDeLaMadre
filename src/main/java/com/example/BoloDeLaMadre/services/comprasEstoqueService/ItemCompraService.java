package com.example.BoloDeLaMadre.services.comprasEstoqueService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.BoloDeLaMadre.dto.comprasEstoqueDto.ItemCompraRequestDTO;
import com.example.BoloDeLaMadre.dto.comprasEstoqueDto.ItemCompraResponseDTO;
import com.example.BoloDeLaMadre.entities.comprasEstoque.Compra;
import com.example.BoloDeLaMadre.entities.comprasEstoque.ItemCompra;
import com.example.BoloDeLaMadre.entities.Ingrediente;
import com.example.BoloDeLaMadre.repositories.comprasEstoqueRepository.ItemCompraRepository;
import com.example.BoloDeLaMadre.repositories.comprasEstoqueRepository.CompraRepository;
import com.example.BoloDeLaMadre.repositories.IngredienteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemCompraService {

    private final ItemCompraRepository itemCompraRepository;
    private final CompraRepository compraRepository;
    private final IngredienteRepository ingredienteRepository;

    public ItemCompraResponseDTO create(UUID compraId, ItemCompraRequestDTO dto) {

        Compra compra = compraRepository.findById(compraId)
                .orElseThrow(() -> new RuntimeException("Compra não encontrada"));

        Ingrediente ingrediente = ingredienteRepository.findById(dto.getIngredienteId())
                .orElseThrow(() -> new RuntimeException("Ingrediente não encontrado"));

        ItemCompra item = new ItemCompra();
        item.setCompra(compra);
        item.setIngrediente(ingrediente);
        item.setQuantidade(dto.getQuantidade().doubleValue());
        item.setPrecoUnitario(dto.getPrecoUnitario());
        item.setSubtotal(dto.getPrecoUnitario().multiply(
                java.math.BigDecimal.valueOf(dto.getQuantidade().doubleValue())
        ));

        itemCompraRepository.save(item);

        return new ItemCompraResponseDTO(item);
    }


    public List<ItemCompraResponseDTO> listByCompra(UUID compraId) {
        return itemCompraRepository.findByCompra_Id(compraId).stream()
                .map(ItemCompraResponseDTO::new)
                .collect(Collectors.toList());
    }
}
