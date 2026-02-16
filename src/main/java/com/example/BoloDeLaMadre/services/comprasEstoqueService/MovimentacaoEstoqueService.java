package com.example.BoloDeLaMadre.services.comprasEstoqueService;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.BoloDeLaMadre.entities.comprasEstoque.MovimentacaoEstoque;
import com.example.BoloDeLaMadre.repositories.comprasEstoqueRepository.MovimentacaoEstoqueRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovimentacaoEstoqueService {

    private final MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    public MovimentacaoEstoque create(MovimentacaoEstoque m) {
        return movimentacaoEstoqueRepository.save(m);
    }

    public List<MovimentacaoEstoque> listAll() {
        return movimentacaoEstoqueRepository.findAll();
    }
}
