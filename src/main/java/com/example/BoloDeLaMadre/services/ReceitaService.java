package com.example.BoloDeLaMadre.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.BoloDeLaMadre.entities.Ingrediente;
import com.example.BoloDeLaMadre.entities.Produto;
import com.example.BoloDeLaMadre.entities.Receita;
import com.example.BoloDeLaMadre.entities.enums.UnidadeMedida;
import com.example.BoloDeLaMadre.excepions.BadRequestException;
import com.example.BoloDeLaMadre.excepions.ResourceNotFoundException;
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

        @Transactional
        public Receita create(UUID produtoId, UUID ingredienteId, Double quantidade, UnidadeMedida unidade) {
                Produto produto = produtoRepository.findById(produtoId)
                                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
                Ingrediente ingrediente = ingredienteRepository.findById(ingredienteId)
                                .orElseThrow(() -> new ResourceNotFoundException("Ingrediente não encontrado"));

                Receita receita = Receita.builder()
                                .produto(produto)
                                .ingrediente(ingrediente)
                                .quantidade(quantidade)
                                .unidade(unidade)
                                .build();

                return receitaRepository.save(receita);
        }

        @Transactional
        public Receita update(UUID id, UUID produtoId, UUID ingredienteId, Double quantidade, UnidadeMedida unidade) {
                Receita receita = receitaRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Receita não encontrada"));

                Produto produto = produtoRepository.findById(produtoId)
                                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
                Ingrediente ingrediente = ingredienteRepository.findById(ingredienteId)
                                .orElseThrow(() -> new ResourceNotFoundException("Ingrediente não encontrado"));

                receita.setProduto(produto);
                receita.setIngrediente(ingrediente);
                receita.setQuantidade(quantidade);
                receita.setUnidade(unidade);

                return receitaRepository.save(receita);
        }

        @Transactional
        public void delete(UUID id) {
                Receita receita = receitaRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Receita não encontrada"));
                receitaRepository.delete(receita);

                if (!receita.getAtivo()) {
                        throw new BadRequestException("Categoria já está inativa");
                }

                receita.setAtivo(false);
                receitaRepository.save(receita);

        }

       
        public Receita getByIdWithProdutoAndIngrediente(UUID id) {
                return receitaRepository.findByIdWithProdutoAndIngrediente(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Receita não encontrada"));
        }

        
        public List<Receita> listAllWithProdutoAndIngrediente() {
                return receitaRepository.findAllWithProdutoAndIngrediente();
        }

}
