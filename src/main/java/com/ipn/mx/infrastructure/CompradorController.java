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

    @GetMapping("/compradores")
    @ResponseStatus(HttpStatus.OK)
    public List<Comprador> readAll() {
        return service.readAll();
    }

    @GetMapping("/compradores/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Comprador read(@PathVariable Integer id) {
        return service.read(id);
    }

    @PostMapping("/compradores")
    @ResponseStatus(HttpStatus.CREATED)
    public Comprador create(@RequestBody Comprador comprador) {
        return service.save(comprador);
    }

    @PutMapping("/compradores/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Comprador update(@PathVariable Integer id, @RequestBody Comprador comprador) {
        Comprador comp = service.read(id);
        comp.setNombre(comprador.getNombre());
        comp.setApellidoPaterno(comprador.getApellidoPaterno());
        comp.setApellidoMaterno(comprador.getApellidoMaterno());
        comp.setCorreo(comprador.getCorreo());
        comp.setContrasenia(comprador.getContrasenia());
        comp.setId_Comprador(comprador.getId_Comprador());
        return service.save(comprador);
    }

    @DeleteMapping("/compradores/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}
