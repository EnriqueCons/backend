package com.ipn.mx.infrastructure;

import com.ipn.mx.domain.entity.Menu;
import com.ipn.mx.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/apiMenu")
public class MenuController {
    @Autowired
    private MenuService service;

    @GetMapping("/productos")
    @ResponseStatus(HttpStatus.OK)
    public List<Menu> readAll() {
        return service.readAll();
    }

    @GetMapping("/productos/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Menu read(@PathVariable Integer id) {
        return service.read(id);
    }

    @PostMapping("/productos")
    @ResponseStatus(HttpStatus.CREATED)
    public Menu create(@RequestBody Menu menu) {
        return service.save(menu);
    }

    @PutMapping("/productos/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Menu update(@PathVariable Integer id, @RequestBody Menu menu) {
        Menu m = service.read(id);
        m.setNombreProducto(menu.getNombreProducto());
        m.setPrecio(menu.getPrecio());
        m.setStock(menu.getStock());
        //m.setId_producto(menu.getId_producto());
        m.setCafeteria(menu.getCafeteria());
        return service.save(m);
    }

    @DeleteMapping("/productos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}
