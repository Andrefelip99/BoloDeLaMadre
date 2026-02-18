package com.example.BoloDeLaMadre.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.BoloDeLaMadre.entities.Funcionario;
import com.example.BoloDeLaMadre.services.FuncionarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/funcionarios")
@RequiredArgsConstructor
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Funcionario create(@RequestBody Funcionario funcionario) {
        return funcionarioService.create(funcionario);
    }

    @PutMapping("/{id}")
    public Funcionario update(@PathVariable UUID id,
            @RequestBody Funcionario funcionario) {
        return funcionarioService.update(id, funcionario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        funcionarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Funcionario> getById(@PathVariable UUID id) {
        Funcionario func = funcionarioService.getById(id);
        return ResponseEntity.ok(func);
    }

    @GetMapping
    public ResponseEntity<List<Funcionario>> listAll() {
        List<Funcionario> funcionarios = funcionarioService.listAll();
        return ResponseEntity.ok(funcionarios);
    }

}
