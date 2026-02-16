package com.example.BoloDeLaMadre.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.example.BoloDeLaMadre.entities.Cliente;
import com.example.BoloDeLaMadre.services.ClienteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente create(@RequestBody Cliente cliente) {
        return clienteService.create(cliente);
    }

    @PutMapping("/{id}")
    public Cliente update(@PathVariable UUID id,
                          @RequestBody Cliente cliente) {
        return clienteService.update(id, cliente);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        clienteService.delete(id);
    }

    @GetMapping("/{id}")
    public Cliente getById(@PathVariable UUID id) {
        return clienteService.getById(id);
    }

    @GetMapping
    public List<Cliente> listAll() {
        return clienteService.listAll();
    }
}
