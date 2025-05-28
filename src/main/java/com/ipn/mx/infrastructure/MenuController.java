package com.ipn.mx.infrastructure;

import com.ipn.mx.domain.entity.Menu;
import com.ipn.mx.service.CafeteriaService;
import com.ipn.mx.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/apiMenu")
public class MenuController {

    @Autowired
    private MenuService service;

    private CafeteriaService cafeteriaService;

    // Obtener todos los productos del menú
    @GetMapping("/productos")
    @ResponseStatus(HttpStatus.OK)
    public List<Menu> readAll() {
        return service.readAll();
    }

    // Obtener un producto por ID
    @GetMapping("/productos/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Menu read(@PathVariable Integer id) {
        Menu menu = service.read(id);
        if (menu == null) {
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }
        return menu;
    }

    // Crear un nuevo producto
    @PostMapping("/productos")
    @ResponseStatus(HttpStatus.CREATED)
    public Menu create(@RequestBody Menu menu) {
        validarMenu(menu);
        return service.save(menu);
    }

    // Actualizar producto existente
    @PutMapping("/productos/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Menu update(@PathVariable Integer id, @RequestBody Menu menu) {
        Menu m = service.read(id);
        if (m == null) {
            throw new RuntimeException("No se puede actualizar: Producto con ID " + id + " no existe.");
        }

        validarMenu(menu);

        m.setNombreProducto(menu.getNombreProducto());
        m.setPrecio(menu.getPrecio());
        m.setStock(menu.getStock());
        m.setCafeteria(menu.getCafeteria());

        return service.save(m);
    }

    // Eliminar producto
    @DeleteMapping("/productos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        Menu m = service.read(id);
        if (m == null) {
            throw new RuntimeException("No se puede eliminar: Producto con ID " + id + " no existe.");
        }
        service.delete(id);
    }

    private void validarMenu(Menu menu) {
        if (menu.getNombreProducto() == null || menu.getNombreProducto().isBlank()) {
            throw new RuntimeException("El nombre del producto es obligatorio.");
        }

        if (menu.getPrecio() == null || menu.getPrecio().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("El precio debe ser mayor o igual a 0.");
        }

        if (menu.getStock() < 0) {
            throw new RuntimeException("El stock no puede ser negativo.");
        }

        if (menu.getCafeteria() == null || menu.getCafeteria().getIdCafeteria() == null) {
            throw new RuntimeException("Debe asignarse una cafetería al producto.");
        }
        if (cafeteriaService.read(menu.getCafeteria().getIdCafeteria()) == null) {
            throw new RuntimeException("La cafetería con ID " + menu.getCafeteria().getIdCafeteria() + " no existe.");
        }
    }



}
