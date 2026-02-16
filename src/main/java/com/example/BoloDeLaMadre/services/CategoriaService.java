package com.example.BoloDeLaMadre.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.BoloDeLaMadre.dto.CategoriaRequestDTO;
import com.example.BoloDeLaMadre.dto.CategoriaResponseDTO;
import com.example.BoloDeLaMadre.entities.Categoria;
import com.example.BoloDeLaMadre.repositories.CategoriaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaResponseDTO create(CategoriaRequestDTO dto) {
        Categoria categoria = new Categoria(dto.getNome(), dto.getDescricao(), true);
        categoriaRepository.save(categoria);
        return new CategoriaResponseDTO(categoria);
    }

    public CategoriaResponseDTO update(UUID id, CategoriaRequestDTO dto) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
        categoria.setNome(dto.getNome());
        categoria.setDescricao(dto.getDescricao());
        categoriaRepository.save(categoria);
        return new CategoriaResponseDTO(categoria);
    }

    public void delete(UUID id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
        categoria.setAtivo(false); 
        categoriaRepository.save(categoria);
    }

    public CategoriaResponseDTO getById(UUID id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
        return new CategoriaResponseDTO(categoria);
    }

    public List<CategoriaResponseDTO> listAll() {
        return categoriaRepository.findAll().stream()
                .map(CategoriaResponseDTO::new)
                .collect(Collectors.toList());
    }
}
