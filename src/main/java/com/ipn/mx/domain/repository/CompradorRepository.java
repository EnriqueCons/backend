package com.ipn.mx.domain.repository;

import com.ipn.mx.domain.entity.Comprador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompradorRepository extends JpaRepository<Comprador, Integer> {
    Optional<Comprador> findByCorreo(String correo);
    Optional<Comprador> findByTokenRecuperacion(String tokenRecuperacion);

}
