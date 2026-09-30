package com.example.BoloDeLaMadre.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.BoloDeLaMadre.dto.AlterarSenhaDTO;
import com.example.BoloDeLaMadre.entities.Funcionario;
import com.example.BoloDeLaMadre.services.FuncionarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/funcionarios")
@RequiredArgsConstructor
public class FuncionarioController {

    private final FuncionarioService funcionarioService;
   

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Funcionario create(@RequestBody Funcionario funcionario) {
        return funcionarioService.create(funcionario);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Funcionario update(@PathVariable UUID id,
            @RequestBody Funcionario funcionario) {
        return funcionarioService.update(id, funcionario);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        funcionarioService.delete(id);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Funcionario getById(@PathVariable UUID id) {
        return funcionarioService.getById(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Funcionario> listAll() {
        return funcionarioService.listAll();
    }

    @PutMapping("/{id}/alterar-senha")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> alterarSenha(@PathVariable UUID id, @RequestBody AlterarSenhaDTO dto) {
        funcionarioService.alterarSenha(id, dto);
        return ResponseEntity.ok("Senha alterada com sucesso");
    }
}
