package com.example.BoloDeLaMadre.dto.comprasEstoqueDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompraRequestDTO {
    @NotNull
    private UUID fornecedorId;
    @NotNull
    private LocalDateTime dataCompra;
    @NotEmpty
    private List<@Valid ItemCompraRequestDTO> itens;
}
