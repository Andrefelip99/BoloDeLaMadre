package com.example.BoloDeLaMadre.services;


import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.BoloDeLaMadre.config.UsuarioDetails;
import com.example.BoloDeLaMadre.repositories.UsuarioRepository;

@Service
public class UsuarioDetailsService implements UserDetailsService {
    private final UsuarioRepository repo;

    public UsuarioDetailsService(UsuarioRepository repo) {
        this.repo = repo;
    }

    @Override
    public UsuarioDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repo.findByUsername(username)
                   .map(UsuarioDetails::new)
                   .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }
}