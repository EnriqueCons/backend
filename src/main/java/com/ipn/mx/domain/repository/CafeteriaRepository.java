package com.ipn.mx.domain.repository;

import com.ipn.mx.domain.entity.Cafeteria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CafeteriaRepository extends JpaRepository<Cafeteria, Integer> {
    Optional<Cafeteria> findByCorreo(String correo);
    Optional<Cafeteria> findByTokenRecuperacion(String tokenRecuperacion);

}
