package com.ipn.mx.infrastructure;

import com.ipn.mx.domain.entity.Cafeteria;
import com.ipn.mx.domain.repository.CafeteriaRepository;
import com.ipn.mx.service.CafeteriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;
import java.util.List;
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


        // Exception para cuando no exista el correo dentro de la tabla cafeteria
        Optional<Cafeteria> opt = cafeteriaRepository.findByCorreo(email);
        if (opt.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "El correo no está registrado en ninguna cafetería");
        }

        service.enviarCorreoRecuperacion(email);
        return "Se ha enviado un enlace de recuperación al correo registrado.";
    }


    //Validar el token
    @GetMapping("/cafeterias/validar-token")
    @ResponseStatus(HttpStatus.OK)
    public boolean validarToken(@RequestParam("token") String token) {
        return service.validarToken(token);
    }

    //Restablecer la nueva contraseña
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

    // Crear nueva cafetería{
    @PostMapping("/cafeterias")
    @ResponseStatus(HttpStatus.CREATED)
    public Cafeteria save(@RequestBody Cafeteria cafeteria) {
        if (cafeteria.getNombre() == null || cafeteria.getNombre().isBlank()) {
            throw new RuntimeException("El nombre de la cafetería es obligatorio.");
        }
        if (cafeteria.getUbicacion() == null || cafeteria.getUbicacion().isBlank()) {
            throw new RuntimeException("La ubicación es obligatoria.");
        }
        if (cafeteria.getHora_inicio() == null || cafeteria.getHora_fin() == null) {
            throw new RuntimeException("Las horas de inicio y fin son obligatorias.");
        }

        return service.save(cafeteria);
    }

    // Actualizar cafetería existente
    @PutMapping("/cafeterias/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Cafeteria update(@RequestBody Cafeteria cafeteria, @PathVariable Integer id) {
        Cafeteria caf = service.read(id);
        if (caf == null) {
            throw new RuntimeException("No se puede actualizar: Cafetería con ID " + id + " no existe.");
        }

        if (cafeteria.getNombre() == null || cafeteria.getNombre().isBlank()) {
            throw new RuntimeException("El nombre de la cafetería es obligatorio.");
        }
        if (cafeteria.getUbicacion() == null || cafeteria.getUbicacion().isBlank()) {
            throw new RuntimeException("La ubicación es obligatoria.");
        }
        if (cafeteria.getHora_inicio() == null || cafeteria.getHora_fin() == null) {
            throw new RuntimeException("Las horas de inicio y fin son obligatorias.");
        }

        caf.setNombre(cafeteria.getNombre());
        caf.setHora_fin(cafeteria.getHora_fin());
        caf.setHora_inicio(cafeteria.getHora_inicio());
        caf.setUbicacion(cafeteria.getUbicacion());
        caf.setCorreo(cafeteria.getCorreo());
        caf.setContrasenia(cafeteria.getContrasenia());

        return service.save(caf);
    }

    // Eliminar cafetería
    @DeleteMapping("/cafeterias/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        Cafeteria caf = service.read(id);
        if (caf == null) {
            throw new RuntimeException("No se puede eliminar: Cafetería con ID " + id + " no existe.");
        }
        service.delete(id);
    }
}
