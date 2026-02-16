package com.example.BoloDeLaMadre.entities.comprasEstoque;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;

import com.example.BoloDeLaMadre.entities.Funcionario;
import com.example.BoloDeLaMadre.entities.Ingrediente;
import com.example.BoloDeLaMadre.entities.enums.TipoMovimentacaoEstoque;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "movimentacoes_estoque")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimentacaoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "ingrediente_id")
    private Ingrediente ingrediente;

    @Enumerated(EnumType.STRING)
    private TipoMovimentacaoEstoque tipo;

    private Double quantidade;
    private BigDecimal custoTotal;
    private UUID referenciaId;
    private String observacao;

    @ManyToOne
    @JoinColumn(name = "funcionario_id")
    private Funcionario funcionario;

    private LocalDateTime createdAt;
}
