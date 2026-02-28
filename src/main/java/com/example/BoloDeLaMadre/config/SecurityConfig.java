package com.example.BoloDeLaMadre.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

import com.example.BoloDeLaMadre.services.UsuarioDetailsService;

@Configuration
@EnableMethodSecurity(prePostEnabled=true)   
public class SecurityConfig {

    private final UsuarioDetailsService userDetailsService;

    public SecurityConfig(UsuarioDetailsService uds) {
        this.userDetailsService = uds;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**")
            )
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())  
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**").hasRole("ADMIN")
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll()
            )
            .userDetailsService(userDetailsService)
            .formLogin(withDefaults())          
            .httpBasic(withDefaults());

        return http.build();
    }
}