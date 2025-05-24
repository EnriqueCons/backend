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

    @GetMapping("/cafeterias")
    @ResponseStatus(HttpStatus.OK)
    public List<Cafeteria> readAll(){
        return service.readAll();
    }

    @GetMapping("/cafeterias/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Cafeteria read(@PathVariable Integer id){
        return service.read(id);
    }

    @PostMapping("/cafeterias")
    @ResponseStatus(HttpStatus.CREATED)
    public Cafeteria create(@RequestBody Cafeteria cafeteria){
        return service.save(cafeteria);
    }

    @PutMapping("/cafeterias/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Cafeteria update(@RequestBody Cafeteria cafeteria, @PathVariable Integer id){
        Cafeteria caf = service.read(id);
        caf.setNombre(cafeteria.getNombre());
        caf.setHora_fin(cafeteria.getHora_fin());
        caf.setHora_inicio(cafeteria.getHora_inicio());
        caf.setUbicacion(cafeteria.getUbicacion());
        //caf.setIdCafeteria(cafeteria.getIdCafeteria());
        return service.save(caf);

    }

    @DeleteMapping("/cafeterias/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id){
        service.delete(id);
    }
}
