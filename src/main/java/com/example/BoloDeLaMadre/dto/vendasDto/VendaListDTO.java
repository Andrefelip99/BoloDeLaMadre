package com.example.BoloDeLaMadre.dto.vendasDto;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.BoloDeLaMadre.entities.enums.StatusVenda;
import com.example.BoloDeLaMadre.entities.vendas.Venda;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VendaListDTO {

    private UUID id;
    private Long numeroVenda;
    private String clienteNome;
    private BigDecimal total;
    private StatusVenda status;

    public VendaListDTO(Venda venda) {
        this.id = venda.getId();
        this.numeroVenda = venda.getNumeroVenda();
        this.total = venda.getTotal();
        this.status = venda.getStatus();
        this.clienteNome = venda.getCliente() != null
                ? venda.getCliente().getNome()
                : null;
    }
}
