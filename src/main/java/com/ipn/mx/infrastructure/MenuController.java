package com.ipn.mx.infrastructure;

import com.ipn.mx.domain.entity.Menu;
import com.ipn.mx.service.CafeteriaService;
import com.ipn.mx.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/apiMenu")
public class MenuController {

    @Autowired
    private MenuService service;

    @Autowired
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

    // Crear producto con imagen
    @PostMapping(value = "/productos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> create(
            @RequestParam("imagen") MultipartFile imagen,
            @RequestParam("nombreProducto") String nombreProducto,
            @RequestParam("precio") String precioStr,
            @RequestParam("precioPuntos") String precioPuntosStr,
            @RequestParam("stock") String stockStr,
            @RequestParam("idCafeteria") String idCafeteriaStr
    ) throws IOException {
        BigDecimal precio = new BigDecimal(precioStr);
        Integer precioPuntos = Integer.parseInt(precioPuntosStr);
        Integer stock = Integer.parseInt(stockStr);
        Integer idCafeteria = Integer.parseInt(idCafeteriaStr);

        Menu menu = Menu.builder()
                .nombreProducto(nombreProducto)
                .precio(precio)
                .precioPuntos(precioPuntos)
                .stock(stock)
                .cafeteria(cafeteriaService.read(idCafeteria))
                .build();

        validarMenu(menu);
        String respuesta = service.saveWithImage(imagen, menu);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    // Actualizar producto con imagen
    @PutMapping(value = "/productos/{id}", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> update(
            @PathVariable Integer id,
            @RequestPart("imagen") MultipartFile imagen,
            @RequestPart("nombreProducto") String nombreProducto,
            @RequestPart("precio") BigDecimal precio,
            @RequestPart("precioPuntos") Integer precioPuntos,
            @RequestPart("stock") Integer stock,
            @RequestPart("idCafeteria") Integer idCafeteria
    ) throws IOException {
        Menu existente = service.read(id);
        if (existente == null) {
            throw new RuntimeException("No se puede actualizar: Producto con ID " + id + " no existe.");
        }

        existente.setNombreProducto(nombreProducto);
        existente.setPrecio(precio);
        existente.setPrecioPuntos(precioPuntos);
        existente.setStock(stock);
        existente.setCafeteria(cafeteriaService.read(idCafeteria));

        validarMenu(existente);
        String respuesta = service.saveWithImage(imagen, existente);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    // Obtener imagen del producto
    @GetMapping("/productos/{id}/imagen")
    public ResponseEntity<byte[]> obtenerImagen(@PathVariable Integer id) {
        Menu menu = service.read(id);
        if (menu == null || menu.getDatosImagen() == null) {
            throw new RuntimeException("Imagen no encontrada para este producto.");
        }

        return ResponseEntity.ok()
                .header("Content-Type", menu.getTipoImagen())
                .body(menu.getDatosImagen());
    }

    // Validar campos del producto
    private void validarMenu(Menu menu) {
        if (menu.getNombreProducto() == null || menu.getNombreProducto().isBlank()) {
            throw new RuntimeException("El nombre del producto es obligatorio.");
        }

        if (menu.getPrecio() == null || menu.getPrecio().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("El precio debe ser mayor o igual a 0.");
        }

        if (menu.getPrecioPuntos() < 0) {
            throw new RuntimeException("El precio en puntos no puede ser negativo.");
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