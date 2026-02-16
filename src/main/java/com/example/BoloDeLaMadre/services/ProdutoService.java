package com.example.BoloDeLaMadre.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.BoloDeLaMadre.entities.Categoria;
import com.example.BoloDeLaMadre.entities.Produto;
import com.example.BoloDeLaMadre.repositories.CategoriaRepository;
import com.example.BoloDeLaMadre.repositories.ProdutoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    @Transactional
    public Produto create(Produto produto, UUID categoriaId) {

        Categoria categoria = null;

        if (categoriaId != null) {
            categoria = categoriaRepository.findById(categoriaId)
                    .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
        }

        produto.setCategoria(categoria);
        produto.setAtivo(true);

        return produtoRepository.save(produto);
    }

    @Transactional
    public Produto update(UUID id, Produto data, UUID categoriaId) {

        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        Categoria categoria = null;

        if (categoriaId != null) {
            categoria = categoriaRepository.findById(categoriaId)
                    .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
        }

        produto.setNome(data.getNome());
        produto.setDescricao(data.getDescricao());
        produto.setPreco(data.getPreco());
        produto.setCustoEstimado(data.getCustoEstimado());
        produto.setMargemLucro(data.getMargemLucro());
        produto.setTamanho(data.getTamanho());
        produto.setPesoKg(data.getPesoKg());
        produto.setFotoUrl(data.getFotoUrl());
        produto.setCategoria(categoria);

        return produtoRepository.save(produto);
    }

    @Transactional
    public void delete(UUID id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        produto.setAtivo(false);
        produtoRepository.save(produto);
    }

    public Produto getById(UUID id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    public List<Produto> listAll() {
        return produtoRepository.findAll();
    }
}
