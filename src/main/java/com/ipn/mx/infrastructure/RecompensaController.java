package com.ipn.mx.infrastructure;

import com.ipn.mx.domain.entity.Recompensa;
import com.ipn.mx.service.RecompensaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/apiRecompensas")
public class RecompensaController {

    @Autowired
    private RecompensaService service;

    @GetMapping("/recompensa")
    @ResponseStatus(HttpStatus.OK)
    public List<Recompensa> readAll() {
        return service.readAll();
    }

    @GetMapping("/recompensa/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Recompensa read(@PathVariable Integer id) {
        Recompensa recompensa = service.read(id);
        if (recompensa == null) {
            throw new RuntimeException("Recompensa no encontrada con ID: " + id);
        }
        return recompensa;
    }

    @PostMapping("/recompensa")
    @ResponseStatus(HttpStatus.CREATED)
    public Recompensa create(@RequestBody Recompensa recompensa) {
        validarRecompensa(recompensa);
        return service.save(recompensa);
    }

    @PutMapping("/recompensa/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Recompensa update(@PathVariable Integer id, @RequestBody Recompensa recompensa) {
        Recompensa r = service.read(id);
        if (r == null) {
            throw new RuntimeException("Recompensa no encontrada con ID: " + id);
        }

        validarRecompensa(recompensa);

        r.setCantidadCompra(recompensa.getCantidadCompra());
        r.setCantidadPuntos(recompensa.getCantidadPuntos());
        r.setCafeteria(recompensa.getCafeteria());

        return service.save(r);
    }

    @DeleteMapping("/recompensa/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        Recompensa recompensa = service.read(id);
        if (recompensa == null) {
            throw new RuntimeException("No se puede eliminar: recompensa con ID " + id + " no existe.");
        }
        service.delete(id);
    }

    // Validación de campos obligatorios
    private void validarRecompensa(Recompensa recompensa) {
        if (recompensa.getCantidadCompra() < 0) {
            throw new RuntimeException("La cantidad de compra debe ser mayor o igual a 0.");
        }

        if (recompensa.getCantidadPuntos() < 0) {
            throw new RuntimeException("La cantidad de puntos no puede ser negativa.");
        }

        if (recompensa.getCafeteria() == null || recompensa.getCafeteria().getIdCafeteria() == null) {
            throw new RuntimeException("Debe asignarse una cafetería válida.");
        }
    }

}
