package com.example.BoloDeLaMadre.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.BoloDeLaMadre.entities.Usuario;

import java.util.Collection;

public class UsuarioDetails implements UserDetails {
    private final Usuario usuario;

    public UsuarioDetails(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return java.util.List.of(new SimpleGrantedAuthority(usuario.getRole()));
    }

    @Override public String getPassword() { return usuario.getPassword(); }
    @Override public String getUsername() { return usuario.getUsername(); }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}