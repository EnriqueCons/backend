package com.ipn.mx.service.impl;

import com.ipn.mx.domain.entity.Cafeteria;
import com.ipn.mx.domain.repository.CafeteriaRepository;
import com.ipn.mx.service.CafeteriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ipn.mx.service.EmailService;
import java.util.List;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CafeteriaServiceImpl implements CafeteriaService {
    @Autowired
    private CafeteriaRepository cafeteriaRepository;

    @Autowired
    private EmailService emailService;


    @Override
    @Transactional
    public void enviarCorreoRecuperacion(String email) {
        Optional<Cafeteria> opt = cafeteriaRepository.findByCorreo(email);
        if (opt.isPresent()) {
            Cafeteria cafeteria = opt.get();

            String token = UUID.randomUUID().toString();
            LocalDateTime expiracion = LocalDateTime.now().plusMinutes(30);

            cafeteria.setTokenRecuperacion(token);
            cafeteria.setFechaExpiracionToken(Timestamp.valueOf(expiracion));

            cafeteriaRepository.save(cafeteria);

            String link = "http://localhost:8082/restablecer?token=" + token; // URL frontend
            String cuerpo = "<html>" +
                    "<body style='font-family: Arial, sans-serif;'>" +
                    "<h2>Hola " + cafeteria.getNombre() + ",</h2>" +
                    "<p>Recibimos una solicitud para restablecer tu contraseña.</p>" +
                    "<p>Haz clic en el siguiente botón para crear una nueva contraseña:</p>" +
                    "<p style='text-align: center; margin: 20px 0;'>" +
                    "<a href='" + link + "' style='background-color: #007bff; color: white; padding: 12px 20px; text-decoration: none; border-radius: 5px;'>Restablecer Contraseña</a>" +
                    "</p>" +
                    "<p>Este enlace expirará en <strong>30 minutos</strong>.</p>" +
                    "<br>" +
                    "<p>Si no solicitaste este cambio, puedes ignorar este mensaje.</p>" +
                    "<p>Saludos,<br>Equipo de soporte</p>" +
                    "</body></html>";

            emailService.enviarCorreo(cafeteria.getCorreo(), "Recuperación de contraseña", cuerpo);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validarToken(String token) {
        Optional<Cafeteria> opt = cafeteriaRepository.findByTokenRecuperacion(token);
        return opt.isPresent() && opt.get().getFechaExpiracionToken().toInstant().isAfter(Instant.now());
    }

    @Override
    @Transactional
    public void actualizarContrasena(String token, String nuevaContrasena) {
        Optional<Cafeteria> opt = cafeteriaRepository.findByTokenRecuperacion(token);
        if (opt.isPresent() && opt.get().getFechaExpiracionToken().toInstant().isAfter(Instant.now())) {
            Cafeteria cafeteria = opt.get();

            // TODO: Cifrar la contraseña con BCrypt (si usas Spring Security)
            cafeteria.setContrasenia(nuevaContrasena);
            cafeteria.setTokenRecuperacion(null);
            cafeteria.setFechaExpiracionToken(null);

            cafeteriaRepository.save(cafeteria);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cafeteria> readAll(){ return cafeteriaRepository.findAll(); }

    @Override
    @Transactional(readOnly = true)
    public Cafeteria read(Integer id) { return cafeteriaRepository.findById(id).orElse(null); }

    @Override
    @Transactional
    public Cafeteria save(Cafeteria cafeteria){ return cafeteriaRepository.save(cafeteria); }

    @Override
    @Transactional
    public void delete(Integer id) { this.cafeteriaRepository.deleteById(id); }
}
