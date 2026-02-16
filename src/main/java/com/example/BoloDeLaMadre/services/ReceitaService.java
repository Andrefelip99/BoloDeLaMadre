package com.example.BoloDeLaMadre.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.BoloDeLaMadre.entities.Ingrediente;
import com.example.BoloDeLaMadre.entities.Produto;
import com.example.BoloDeLaMadre.entities.Receita;
import com.example.BoloDeLaMadre.entities.enums.UnidadeMedida;
import com.example.BoloDeLaMadre.repositories.IngredienteRepository;
import com.example.BoloDeLaMadre.repositories.ProdutoRepository;
import com.example.BoloDeLaMadre.repositories.ReceitaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReceitaService {

    private final ReceitaRepository receitaRepository;
    private final ProdutoRepository produtoRepository;
    private final IngredienteRepository ingredienteRepository;

    // CRIA UMA RECEITA
    @Transactional
    public Receita create(UUID produtoId, UUID ingredienteId, Double quantidade, UnidadeMedida unidade) {
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        Ingrediente ingrediente = ingredienteRepository.findById(ingredienteId)
                .orElseThrow(() -> new RuntimeException("Ingrediente não encontrado"));

        Receita receita = Receita.builder()
                .produto(produto)
                .ingrediente(ingrediente)
                .quantidade(quantidade)
                .unidade(unidade)
                .build();

        return receitaRepository.save(receita);
    }

    // ATUALIZA UMA RECEITA EXISTENTE
    @Transactional
    public Receita update(UUID id, UUID produtoId, UUID ingredienteId, Double quantidade, UnidadeMedida unidade) {
        Receita receita = receitaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Receita não encontrada"));

        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        Ingrediente ingrediente = ingredienteRepository.findById(ingredienteId)
                .orElseThrow(() -> new RuntimeException("Ingrediente não encontrado"));

        receita.setProduto(produto);
        receita.setIngrediente(ingrediente);
        receita.setQuantidade(quantidade);
        receita.setUnidade(unidade);

        return receitaRepository.save(receita);
    }

    // REMOVE UMA RECEITA
    @Transactional
    public void delete(UUID id) {
        Receita receita = receitaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Receita não encontrada"));
        receitaRepository.delete(receita);
    }

    // BUSCA UMA RECEITA POR ID
    public Receita getById(UUID id) {
        return receitaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Receita não encontrada"));
    }

    // LISTA TODAS AS RECEITAS
    public List<Receita> listAll() {
        return receitaRepository.findAll();
    }
}
