package com.ipn.mx.service;

import com.ipn.mx.domain.entity.Cafeteria;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CafeteriaService {
    List<Cafeteria> readAll();
    Cafeteria read(Integer id);
    Cafeteria save(Cafeteria cafeteria);
    void delete(Integer id);

    void enviarCorreoRecuperacion(String email);
    boolean validarToken(String token);
    void actualizarContrasena(String token, String nuevaContrasena);

    // Nuevo método para subir imagen de cafetería
    String saveWithImage(MultipartFile imagen, Cafeteria cafeteria) throws IOException;
}
