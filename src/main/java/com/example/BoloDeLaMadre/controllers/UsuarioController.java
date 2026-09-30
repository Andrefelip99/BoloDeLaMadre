package com.example.BoloDeLaMadre.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BoloDeLaMadre.dto.UsuarioRequestDTO;
import com.example.BoloDeLaMadre.entities.Usuario;
import com.example.BoloDeLaMadre.services.UsuarioService;

import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public Map<String, String> currentUser(Authentication authentication) {
        String role = authentication.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .filter(authority -> authority.startsWith("ROLE_"))
                .map(authority -> authority.substring("ROLE_".length()))
                .findFirst()
                .orElse("USER");

        return Map.of("username", authentication.getName(), "role", role);
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
