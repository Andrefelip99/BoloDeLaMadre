package com.example.BoloDeLaMadre.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.BoloDeLaMadre.entities.Funcionario;
import com.example.BoloDeLaMadre.excepions.BadRequestException;
import com.example.BoloDeLaMadre.excepions.ResourceNotFoundException;
import com.example.BoloDeLaMadre.repositories.FuncionarioRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;

    public Funcionario create(Funcionario funcionario) {
        funcionario.setAtivo(true);
        return funcionarioRepository.save(funcionario);
    }

    public Funcionario update(UUID id, Funcionario data) {
        Funcionario func = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionario não encontrado"));

        func.setNome(data.getNome());
        func.setEmail(data.getEmail());
        func.setTelefone(data.getTelefone());
        func.setPapel(data.getPapel());
        func.setSalario(data.getSalario());

        return funcionarioRepository.save(func);
    }

    @Transactional
    public void delete(UUID id) {
        Funcionario func = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionario não encontrado"));

        if (!func.getAtivo()) {
            throw new BadRequestException("Funcionario já está inativo");
        }

        func.setAtivo(false);
        funcionarioRepository.save(func);
    }

    public Funcionario getById(UUID id) {
        return funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionario não encontrado"));
    }

    public List<Funcionario> listAll() {
        return funcionarioRepository.findAll();
    }

}
