package com.example.BoloDeLaMadre.entities.vendas;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.example.BoloDeLaMadre.entities.BaseEntity;
import com.example.BoloDeLaMadre.entities.Cliente;
import com.example.BoloDeLaMadre.entities.Funcionario;
import com.example.BoloDeLaMadre.entities.enums.CanalVenda;
import com.example.BoloDeLaMadre.entities.enums.FormaPagamento;
import com.example.BoloDeLaMadre.entities.enums.StatusVenda;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vendas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venda extends BaseEntity {

    private Long numeroVenda;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "funcionario_id")
    private Funcionario funcionario;

    @Enumerated(EnumType.STRING)
    private CanalVenda canal;

    @Enumerated(EnumType.STRING)
    private StatusVenda status;

    @Enumerated(EnumType.STRING)
    private FormaPagamento formaPagamento;

    private BigDecimal subtotal;
    private BigDecimal desconto;
    private BigDecimal taxaEntrega;
    private BigDecimal taxaPlataforma;
    private BigDecimal total;

    private LocalDateTime dataVenda;
    private LocalDateTime dataEntrega;

   
    
    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL)
    private List<ItemVenda> itens;
}
