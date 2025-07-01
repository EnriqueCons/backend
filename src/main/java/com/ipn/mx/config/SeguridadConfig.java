package com.ipn.mx.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SeguridadConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/apiComprador/compradores", // 👈 permitir registro sin login
                                "/apiComprador/enviar-token",
                                "/apiComprador/restablecer-contrasena",
                                "/apiComprador/validar-token",
                                "/apiCafeteria/**",
                                "/restablecer", // si usas esta ruta para frontend también
                                "/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
