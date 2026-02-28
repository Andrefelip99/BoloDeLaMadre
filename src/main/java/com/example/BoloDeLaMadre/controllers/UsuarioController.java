package com.example.BoloDeLaMadre.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BoloDeLaMadre.dto.UsuarioRequestDTO;
import com.example.BoloDeLaMadre.entities.Usuario;
import com.example.BoloDeLaMadre.services.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@RequestBody UsuarioRequestDTO dto) {
        if (dto.getUsername() == null || dto.getPassword() == null) {
            return ResponseEntity.badRequest().body("username and password required");
        }

        if (service.exists(dto.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Usuário já existe");
        }

        String role = dto.getRole() != null ? dto.getRole() : "ROLE_USER";
        Usuario created = service.createIfNotExists(dto.getUsername(), dto.getPassword(), role);
        return ResponseEntity.status(HttpStatus.CREATED).body(java.util.Map.of("id", created.getId(), "username", created.getUsername()));
    }
}
