package com.ipn.mx.infrastructure;


import com.ipn.mx.domain.entity.Recompensa;
import com.ipn.mx.service.RecompensaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/apiRecompensas")
public class RecompensaController {
    @Autowired
    private RecompensaService service;

    @GetMapping("/recompensa")
    @ResponseStatus(HttpStatus.OK)
    public List<Recompensa> readAll(){
        return service.readAll();
    }

    @GetMapping("/recompensa/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Recompensa read(@PathVariable Integer id){
        return service.read(id);
    }

    @PostMapping("/recompensa")
    @ResponseStatus(HttpStatus.CREATED)
    public Recompensa create(@RequestBody Recompensa recompensa){
        return service.save(recompensa);
    }

    @PutMapping("/recompensa/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Recompensa update(@PathVariable Integer id, @RequestBody Recompensa recompensa){
        Recompensa r = service.read(id);
        r.setCantidadCompra(recompensa.getCantidadCompra());
        r.setCantidadPuntos(recompensa.getCantidadPuntos());
        //r.setId_recompensa(recompensa.getId_recompensa());
        r.setCafeteria(recompensa.getCafeteria());
        return service.save(r);
    }

    @DeleteMapping("/recompensa/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id){
        service.delete(id);
    }
}
