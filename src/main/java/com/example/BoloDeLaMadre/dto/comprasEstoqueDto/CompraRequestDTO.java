package com.example.BoloDeLaMadre.dto.comprasEstoqueDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompraRequestDTO {

    private UUID fornecedorId;
    private LocalDateTime dataCompra;
    private List<ItemCompraRequestDTO> itens;
}
