package com.example.BoloDeLaMadre.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.BoloDeLaMadre.entities.Funcionario;
import com.example.BoloDeLaMadre.entities.enums.PapelUsuario;
import com.example.BoloDeLaMadre.repositories.FuncionarioRepository;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class AdminInitializer implements ApplicationRunner {

    private final FuncionarioRepository funcionarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.senha}")
    private String adminSenha;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (funcionarioRepository.findByEmail(adminEmail).isEmpty()) {
            Funcionario admin = Funcionario.builder()
                    .email(adminEmail)
                    .senha(passwordEncoder.encode(adminSenha))
                    .nome("Administrador")
                    .papel(PapelUsuario.ADMIN)
                    .ativo(true)
                    .build();

            funcionarioRepository.save(admin);
            System.out.println("Admin inicial criado!");
        }
    }
}

