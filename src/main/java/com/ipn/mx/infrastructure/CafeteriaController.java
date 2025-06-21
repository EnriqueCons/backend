package com.ipn.mx.infrastructure;

import com.ipn.mx.domain.entity.Cafeteria;
import com.ipn.mx.service.CafeteriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/apiCafeteria")
public class CafeteriaController {

    @Autowired
    private CafeteriaService service;

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
