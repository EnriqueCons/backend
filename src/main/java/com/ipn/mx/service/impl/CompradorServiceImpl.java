package com.ipn.mx.service.impl;

import com.ipn.mx.domain.entity.Comprador;
import com.ipn.mx.domain.repository.CompradorRepository;
import com.ipn.mx.service.CompradorService;
import com.ipn.mx.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;




import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CompradorServiceImpl implements CompradorService {
    @Autowired
    private CompradorRepository compradorRepository;
    @Autowired
    private EmailService emailService;


    @Override
    @Transactional
    public void enviarCorreoRecuperacion(String email) {
        Optional<Comprador> opt = compradorRepository.findByCorreo(email);
        if (opt.isPresent()) {
            Comprador comprador = opt.get();

            String token = UUID.randomUUID().toString();
            LocalDateTime expiracion = LocalDateTime.now().plusMinutes(30);

            comprador.setTokenRecuperacion(token);
            comprador.setFechaExpiracionToken(Timestamp.valueOf(expiracion));

            compradorRepository.save(comprador);

            String link = "https://backend-o9xo.onrender.com/restablecer?token=" + token; // URL frontend
            String cuerpo = "<html>" +
                    "<body style='font-family: Arial, sans-serif;'>" +
                    "<h2>Hola " + comprador.getNombre() + ",</h2>" +
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

            emailService.enviarCorreo(comprador.getCorreo(), "Recuperación de contraseña", cuerpo);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validarToken(String token) {
        Optional<Comprador> opt = compradorRepository.findByTokenRecuperacion(token);
        return opt.isPresent() && opt.get().getFechaExpiracionToken().toInstant().isAfter(Instant.now());
    }

    @Override
    @Transactional
    public void actualizarContrasena(String token, String nuevaContrasena) {
        Optional<Comprador> opt = compradorRepository.findByTokenRecuperacion(token);
        if (opt.isPresent() && opt.get().getFechaExpiracionToken().toInstant().isAfter(Instant.now())) {
            Comprador comprador = opt.get();

            // TODO: Cifrar la contraseña con BCrypt (si usas Spring Security)
            // comprador.setContrasenia(passwordEncoder.encode(nuevaContrasena));
            comprador.setContrasenia(nuevaContrasena);
            comprador.setTokenRecuperacion(null);
            comprador.setFechaExpiracionToken(null);

            compradorRepository.save(comprador);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comprador> readAll(){
        return compradorRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Comprador read(Integer id){
        return compradorRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Comprador save(Comprador comprador){
        return compradorRepository.save(comprador);
    }

    @Override
    @Transactional
    public void delete(Integer id){
        compradorRepository.deleteById(id);
    }
}
