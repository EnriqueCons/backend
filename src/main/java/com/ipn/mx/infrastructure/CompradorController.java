package com.ipn.mx.infrastructure;

import com.ipn.mx.domain.entity.Comprador;
import com.ipn.mx.service.CompradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/apiComprador")
public class CompradorController {

    @Autowired
    private CompradorService service;

    // Obtener todos los compradores
    @GetMapping("/compradores")
    @ResponseStatus(HttpStatus.OK)
    public List<Comprador> readAll() {
        return service.readAll();
    }

    // Obtener un comprador por ID
    @GetMapping("/compradores/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Comprador read(@PathVariable Integer id) {
        Comprador comprador = service.read(id);
        if (comprador == null) {
            throw new RuntimeException("Comprador no encontrado con ID: " + id);
        }
        return comprador;
    }

    // Crear nuevo comprador
    @PostMapping("/compradores")
    @ResponseStatus(HttpStatus.CREATED)
    public Comprador create(@RequestBody Comprador comprador) {
        validarComprador(comprador);
        return service.save(comprador);
    }

    // Actualizar comprador existente
    @PutMapping("/compradores/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Comprador update(@PathVariable Integer id, @RequestBody Comprador comprador) {
        Comprador comp = service.read(id);
        if (comp == null) {
            throw new RuntimeException("No se puede actualizar: Comprador con ID " + id + " no existe.");
        }

        validarComprador(comprador);

        comp.setNombre(comprador.getNombre());
        comp.setApellidoPaterno(comprador.getApellidoPaterno());
        comp.setApellidoMaterno(comprador.getApellidoMaterno());
        comp.setCorreo(comprador.getCorreo());
        comp.setContrasenia(comprador.getContrasenia());

        return service.save(comp);
    }

    // Eliminar comprador
    @DeleteMapping("/compradores/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        Comprador comp = service.read(id);
        if (comp == null) {
            throw new RuntimeException("No se puede eliminar: Comprador con ID " + id + " no existe.");
        }
        service.delete(id);
    }

    // Validación reutilizable
    private void validarComprador(Comprador c) {
        if (c.getNombre() == null || c.getNombre().isBlank()) {
            throw new RuntimeException("El nombre es obligatorio.");
        }
        if (c.getApellidoPaterno() == null || c.getApellidoPaterno().isBlank()) {
            throw new RuntimeException("El apellido paterno es obligatorio.");
        }
        if (c.getCorreo() == null || c.getCorreo().isBlank()) {
            throw new RuntimeException("El correo es obligatorio.");
        }
        if (c.getContrasenia() == null || c.getContrasenia().isBlank()) {
            throw new RuntimeException("La contraseña es obligatoria.");
        }
    }
}
