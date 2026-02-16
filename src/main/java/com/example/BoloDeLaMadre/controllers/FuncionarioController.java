package com.example.BoloDeLaMadre.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        funcionarioService.delete(id);
    }

    @GetMapping("/{id}")
    public Funcionario getById(@PathVariable UUID id) {
        return funcionarioService.getById(id);
    }

    @GetMapping
    public List<Funcionario> listAll() {
        return funcionarioService.listAll();
    }
}
