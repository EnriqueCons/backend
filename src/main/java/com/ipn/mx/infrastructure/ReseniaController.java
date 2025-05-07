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
    public List<Resenia> readAll(){
        return service.readAll();
    }

    @GetMapping("/resenia/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Resenia read(@PathVariable Integer id){
        return service.read(id);
    }

    @PostMapping("/resenia")
    @ResponseStatus(HttpStatus.CREATED)
    public Resenia create(@RequestBody Resenia resenia){
        return service.save(resenia);
    }

    @PutMapping("/resenia/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Resenia update(@PathVariable Integer id, @RequestBody Resenia resenia){
        Resenia r = service.read(id);
        r.setIdResena(resenia.getIdResena());
        r.setComentario(resenia.getComentario());
        r.setCalificacion(resenia.getCalificacion());
        r.setFecha(resenia.getFecha());
        r.setPedido(resenia.getPedido());
        r.setComprador(resenia.getComprador());
        return service.save(r);
    }

    @DeleteMapping("/resenia/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id){
        service.delete(id);
    }
}
