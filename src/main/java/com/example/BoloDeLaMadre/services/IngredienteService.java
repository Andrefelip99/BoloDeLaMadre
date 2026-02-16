package com.example.BoloDeLaMadre.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.BoloDeLaMadre.entities.Fornecedor;
import com.example.BoloDeLaMadre.entities.Ingrediente;
import com.example.BoloDeLaMadre.entities.enums.UnidadeMedida;
import com.example.BoloDeLaMadre.repositories.FornecedorRepository;
import com.example.BoloDeLaMadre.repositories.IngredienteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IngredienteService {

    private final IngredienteRepository ingredienteRepository;
    private final FornecedorRepository fornecedorRepository;

    @Transactional
    public Ingrediente create(String nome, UnidadeMedida unidade, BigDecimal custoUnitario,
            Double estoqueAtual, Double estoqueMinimo, UUID fornecedorId) {

        Fornecedor fornecedor = fornecedorId != null
                ? fornecedorRepository.findById(fornecedorId)
                        .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"))
                : null;

        Ingrediente ing = Ingrediente.builder()
                .nome(nome)
                .unidade(unidade)
                .custoUnitario(custoUnitario)
                .estoqueAtual(estoqueAtual)
                .estoqueMinimo(estoqueMinimo)
                .fornecedor(fornecedor)
                .ativo(true)
                .build();

        return ingredienteRepository.save(ing);
    }

    @Transactional
    public Ingrediente update(UUID id, String nome, UnidadeMedida unidade, BigDecimal custoUnitario,
            Double estoqueAtual, Double estoqueMinimo, UUID fornecedorId) {

        Ingrediente ing = ingredienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingrediente não encontrado"));

        Fornecedor fornecedor = null;
        if (fornecedorId != null) {
            fornecedor = fornecedorRepository.findById(fornecedorId)
                    .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));
        }

        ing.setNome(nome);
        ing.setUnidade(unidade);
        ing.setCustoUnitario(custoUnitario);
        ing.setEstoqueAtual(estoqueAtual);
        ing.setEstoqueMinimo(estoqueMinimo);
        ing.setFornecedor(fornecedor);

        return ingredienteRepository.save(ing);
    }

    @Transactional
    public void delete(UUID id) {
        Ingrediente ing = ingredienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingrediente não encontrado"));
        ing.setAtivo(false);
        ingredienteRepository.save(ing);
    }

    public Ingrediente getById(UUID id) {
        return ingredienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingrediente não encontrado"));
    }

    public List<Ingrediente> listAll() {
        return ingredienteRepository.findAll();
    }
}
