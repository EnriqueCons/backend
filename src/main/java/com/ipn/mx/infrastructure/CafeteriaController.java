package com.ipn.mx.infrastructure;

import com.ipn.mx.domain.entity.Cafeteria;
import com.ipn.mx.domain.repository.CafeteriaRepository;
import com.ipn.mx.service.CafeteriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/apiCafeteria")
public class CafeteriaController {

    @Autowired
    private CafeteriaService service;

    @Autowired
    private CafeteriaRepository cafeteriaRepository;

    // Enviar correo para recuperar la contraseña
    @PostMapping("/cafeterias/recuperar")
    @ResponseStatus(HttpStatus.OK)
    public String recuperarContrasena(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Optional<Cafeteria> opt = cafeteriaRepository.findByCorreo(email);
        if (opt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El correo no está registrado en ninguna cafetería");
        }
        service.enviarCorreoRecuperacion(email);
        return "Se ha enviado un enlace de recuperación al correo registrado.";
    }

    // Validar token
    @GetMapping("/cafeterias/validar-token")
    @ResponseStatus(HttpStatus.OK)
    public boolean validarToken(@RequestParam("token") String token) {
        return service.validarToken(token);
    }

    // Restablecer contraseña
    @PostMapping("/cafeterias/restablecer")
    @ResponseStatus(HttpStatus.OK)
    public String restablecerContrasena(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String nuevaContrasena = request.get("nuevaContrasena");
        if (service.validarToken(token)) {
            service.actualizarContrasena(token, nuevaContrasena);
            return "Contraseña actualizada con éxito.";
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token inválido o expirado.");
        }
    }

    // Obtener todas las cafeterías
    @GetMapping("/cafeterias")
    @ResponseStatus(HttpStatus.OK)
    public List<Cafeteria> readAll() {
        return service.readAll();
    }

    // Obtener una cafetería por ID
    @GetMapping("/cafeterias/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Cafeteria read(@PathVariable Integer id) {
        Cafeteria cafeteria = service.read(id);
        if (cafeteria == null) {
            throw new RuntimeException("Cafetería no encontrada con ID: " + id);
        }
        return cafeteria;
    }

    // Crear nueva cafetería con imagen
    @PostMapping(value = "/cafeterias", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> save(
            @RequestPart("imagen") MultipartFile imagen,
            @RequestPart("nombre") String nombre,
            @RequestPart("ubicacion") String ubicacion,
            @RequestPart("horaInicio") String horaInicio,
            @RequestPart("horaFin") String horaFin,
            @RequestPart("correo") String correo,
            @RequestPart("contrasenia") String contrasenia
    ) throws IOException {
        Cafeteria cafeteria = Cafeteria.builder()
                .nombre(nombre)
                .ubicacion(ubicacion)
                .hora_inicio(LocalTime.parse(horaInicio))
                .hora_fin(LocalTime.parse(horaFin))
                .correo(correo)
                .contrasenia(contrasenia)
                .build();

        String respuesta = service.saveWithImage(imagen, cafeteria);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    // Actualizar cafetería con imagen
    @PutMapping(value = "/cafeterias" +
            "/{id}", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> update(
            @PathVariable Integer id,
            @RequestPart("imagen") MultipartFile imagen,
            @RequestPart("nombre") String nombre,
            @RequestPart("ubicacion") String ubicacion,
            @RequestPart("horaInicio") String horaInicio,
            @RequestPart("horaFin") String horaFin,
            @RequestPart("correo") String correo,
            @RequestPart("contrasenia") String contrasenia
    ) throws IOException {
        Cafeteria existente = service.read(id);
        if (existente == null) {
            throw new RuntimeException("No se puede actualizar: Cafetería con ID " + id + " no existe.");
        }

        existente.setNombre(nombre);
        existente.setUbicacion(ubicacion);
        existente.setHora_inicio(LocalTime.parse(horaInicio));
        existente.setHora_fin(LocalTime.parse(horaFin));
        existente.setCorreo(correo);
        existente.setContrasenia(contrasenia);

        String respuesta = service.saveWithImage(imagen, existente);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    // Eliminar cafetería
    @DeleteMapping("/cafeterias/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        Cafeteria cafeteria = service.read(id);
        if (cafeteria == null) {
            throw new RuntimeException("No se puede eliminar: Cafetería con ID " + id + " no existe.");
        }
        service.delete(id);
    }

    // Obtener imagen de una cafetería
    @GetMapping("/cafeterias/{id}/imagen")
    public ResponseEntity<byte[]> obtenerImagen(@PathVariable Integer id) {
        Cafeteria cafeteria = service.read(id);
        if (cafeteria == null || cafeteria.getDatosImagen() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Imagen no encontrada para esta cafetería.");
        }

        return ResponseEntity.ok()
                .header("Content-Type", cafeteria.getTipoImagen())
                .body(cafeteria.getDatosImagen());
    }
}
