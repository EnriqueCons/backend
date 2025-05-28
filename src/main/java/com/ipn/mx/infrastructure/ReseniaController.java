package com.ipn.mx.infrastructure;

import com.ipn.mx.domain.entity.Resenia;
import com.ipn.mx.service.ReseniaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/apiResenias")
public class ReseniaController {

    @Autowired
    private ReseniaService service;

    @GetMapping("/resenia")
    @ResponseStatus(HttpStatus.OK)
    public List<Resenia> readAll() {
        return service.readAll();
    }

    @GetMapping("/resenia/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Resenia read(@PathVariable Integer id) {
        Resenia resenia = service.read(id);
        if (resenia == null) {
            throw new RuntimeException("Reseña no encontrada con ID: " + id);
        }
        return resenia;
    }

    @PostMapping("/resenia")
    @ResponseStatus(HttpStatus.CREATED)
    public Resenia create(@RequestBody Resenia resenia) {
        validarResenia(resenia);
        return service.save(resenia);
    }

    @PutMapping("/resenia/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Resenia update(@PathVariable Integer id, @RequestBody Resenia resenia) {
        Resenia r = service.read(id);
        if (r == null) {
            throw new RuntimeException("Reseña no encontrada con ID: " + id);
        }

        validarResenia(resenia);

        r.setComentario(resenia.getComentario());
        r.setCalificacion(resenia.getCalificacion());
        r.setFecha(resenia.getFecha());
        r.setPedido(resenia.getPedido());
        r.setComprador(resenia.getComprador());

        return service.save(r);
    }

    @DeleteMapping("/resenia/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        Resenia r = service.read(id);
        if (r == null) {
            throw new RuntimeException("No se puede eliminar: reseña con ID " + id + " no existe.");
        }
        service.delete(id);
    }

    // Validación básica de campos obligatorios
    private void validarResenia(Resenia resenia) {
        if (resenia.getComentario() == null || resenia.getComentario().isBlank()) {
            throw new RuntimeException("El comentario no puede estar vacío.");
        }
        if (resenia.getCalificacion() < 0 || resenia.getCalificacion() > 5) {
            throw new RuntimeException("La calificación debe estar entre 0 y 5.");
        }
        if (resenia.getFecha() == null) {
            throw new RuntimeException("La fecha es obligatoria.");
        }
        if (resenia.getPedido() == null || resenia.getPedido().getNo_orden() == null) {
            throw new RuntimeException("Debe asociarse un pedido válido.");
        }
        if (resenia.getComprador() == null || resenia.getComprador().getId_Comprador() == null) {
            throw new RuntimeException("Debe asociarse un comprador válido.");
        }
    }
}
