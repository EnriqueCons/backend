package com.ipn.mx.infrastructure;

import com.ipn.mx.domain.entity.LoginRequest;
import com.ipn.mx.domain.entity.LoginResponse;
import com.ipn.mx.domain.entity.Cafeteria;
import com.ipn.mx.domain.entity.Comprador;
import com.ipn.mx.domain.repository.CafeteriaRepository;
import com.ipn.mx.domain.repository.CompradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/apiAuth")
public class AuthController {

    @Autowired
    private CafeteriaRepository cafeteriaRepository;

    @Autowired
    private CompradorRepository compradorRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String correo = loginRequest.getCorreo();
        String contrasenia = loginRequest.getContrasenia();

        // Buscar en cafetería
        Optional<Cafeteria> cafeteria = cafeteriaRepository.findByCorreo(correo);
        if (cafeteria.isPresent()) {
            if (cafeteria.get().getContrasenia().equals(contrasenia)) {
                return ResponseEntity.ok(new LoginResponse("cafeteria",
                        cafeteria.get().getIdCafeteria(),
                        cafeteria.get().getNombre()));
            }
        }

        // Buscar en comprador
        Optional<Comprador> comprador = compradorRepository.findByCorreo(correo);
        if (comprador.isPresent()) {
            if (comprador.get().getContrasenia().equals(contrasenia)) {
                String nombreCompleto = comprador.get().getNombre() + " " + comprador.get().getApellidoPaterno();
                return ResponseEntity.ok(new LoginResponse("comprador",
                        comprador.get().getId_Comprador(),
                        nombreCompleto));
            }
        }

        // Si no se encuentra el usuario
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Correo o contraseña incorrectos");
    }
}
