package com.example.BoloDeLaMadre.dto.vendasDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.example.BoloDeLaMadre.entities.enums.StatusVenda;
import com.example.BoloDeLaMadre.entities.vendas.Venda;

import lombok.Getter;

@Getter
public class VendaDetailsDTO {

    private UUID id;
    private Long numeroVenda;

    private String clienteNome;
    private String funcionarioNome;

    private BigDecimal subtotal;
    private BigDecimal desconto;
    private BigDecimal total;

    private StatusVenda status;

    private List<ItemVendaResponseDTO> itens;

    public VendaDetailsDTO(Venda venda, List<ItemVendaResponseDTO> itens) {

        this.id = venda.getId();
        this.numeroVenda = venda.getNumeroVenda();

        this.subtotal = venda.getSubtotal();
        this.desconto = venda.getDesconto();
        this.total = venda.getTotal();
        this.status = venda.getStatus();

        this.clienteNome = venda.getCliente() != null
                ? venda.getCliente().getNome()
                : null;

        this.funcionarioNome = venda.getFuncionario() != null
                ? venda.getFuncionario().getNome()
                : null;

        this.itens = itens;
    }
}
