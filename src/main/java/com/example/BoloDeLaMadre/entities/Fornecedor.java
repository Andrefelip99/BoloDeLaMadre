package com.example.BoloDeLaMadre.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fornecedores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fornecedor extends BaseEntity{
    
    private String nome;
    private String telefone;
    private String email;
    private String endereco;
    private String cnpj;
    private String observacoes;
    private Boolean ativo;

    

}
