package com.example.BoloDeLaMadre.repositories;

import com.example.BoloDeLaMadre.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,String> {
    Optional<Usuario> findByUsername(String username);
}
