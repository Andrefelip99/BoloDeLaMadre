package com.example.BoloDeLaMadre.services;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.example.BoloDeLaMadre.entities.Fornecedor;
import com.example.BoloDeLaMadre.excepions.BadRequestException;
import com.example.BoloDeLaMadre.excepions.ResourceNotFoundException;
import com.example.BoloDeLaMadre.repositories.FornecedorRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;

    public Fornecedor create(Fornecedor fornecedor) {
        fornecedor.setAtivo(true);
        return fornecedorRepository.save(fornecedor);
    }

    public Fornecedor update(UUID id, Fornecedor dados) {

        Fornecedor fornecedor = fornecedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado"));

        fornecedor.setNome(dados.getNome());
        fornecedor.setTelefone(dados.getTelefone());
        fornecedor.setEmail(dados.getEmail());
        fornecedor.setEndereco(dados.getEndereco());
        fornecedor.setCnpj(dados.getCnpj());
        fornecedor.setObservacoes(dados.getObservacoes());

        return fornecedorRepository.save(fornecedor);
    }

    public void delete(UUID id) {
        Fornecedor fornecedor = fornecedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado"));

        if (!fornecedor.getAtivo()) {
            throw new BadRequestException("Fornecedor já está inativo");
        }

        fornecedor.setAtivo(false);
        fornecedorRepository.save(fornecedor);
    }

    public Fornecedor getById(UUID id) {
        return fornecedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado"));
    }

    public List<Fornecedor> listAll() {
        return fornecedorRepository.findAll();
    }

}
