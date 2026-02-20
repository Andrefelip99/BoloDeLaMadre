package com.example.BoloDeLaMadre.services;

import java.math.BigDecimal;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.BoloDeLaMadre.entities.Fornecedor;
import com.example.BoloDeLaMadre.entities.Ingrediente;
import com.example.BoloDeLaMadre.entities.enums.UnidadeMedida;
import com.example.BoloDeLaMadre.excepions.BadRequestException;
import com.example.BoloDeLaMadre.excepions.ResourceNotFoundException;
import com.example.BoloDeLaMadre.repositories.FornecedorRepository;
import com.example.BoloDeLaMadre.repositories.IngredienteRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IngredienteService {

        private final IngredienteRepository ingredienteRepository;
        private final FornecedorRepository fornecedorRepository;

        @Transactional
        public Ingrediente create(String nome,
                        UnidadeMedida unidade,
                        BigDecimal custoUnitario,
                        Double estoqueAtual,
                        Double estoqueMinimo,
                        UUID fornecedorId) {

                Fornecedor fornecedor = buscarFornecedorSeInformado(fornecedorId);

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
        public Ingrediente update(UUID id,
                        String nome,
                        UnidadeMedida unidade,
                        BigDecimal custoUnitario,
                        Double estoqueAtual,
                        Double estoqueMinimo,
                        UUID fornecedorId) {

                Ingrediente ing = buscarIngrediente(id);

                if (!ing.getAtivo()) {
                        throw new BadRequestException("Ingrediente está inativo");
                }

                Fornecedor fornecedor = buscarFornecedorSeInformado(fornecedorId);

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

                Ingrediente ing = buscarIngrediente(id);

                if (!ing.getAtivo()) {
                        throw new BadRequestException("Ingrediente já está inativo");
                }

                ing.setAtivo(false);
                ingredienteRepository.save(ing);
        }

        public Ingrediente getById(UUID id) {
                return ingredienteRepository.findByIdWithFornecedor(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Ingrediente não encontrado"));
        }

        public List<Ingrediente> listAll() {
                return ingredienteRepository.findAllWithFornecedor();
        }

        public long countEstoqueBaixo() {
                return ingredienteRepository.countByEstoqueAbaixoDoMinimo();
        }

       

        private Ingrediente buscarIngrediente(UUID id) {
                return ingredienteRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Ingrediente não encontrado"));
        }

        private Fornecedor buscarFornecedorSeInformado(UUID fornecedorId) {

                if (fornecedorId == null) {
                        return null;
                }

                return fornecedorRepository.findById(fornecedorId)
                                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado"));
        }
}
