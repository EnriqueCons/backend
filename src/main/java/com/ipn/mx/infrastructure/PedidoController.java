package com.ipn.mx.infrastructure;

import com.ipn.mx.domain.entity.Cafeteria;
import com.ipn.mx.domain.entity.DetallePedido;
import com.ipn.mx.domain.entity.Pedido;
import com.ipn.mx.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/apiPedido")
public class PedidoController {

    @Autowired
    private PedidoService service;
    @Autowired
    private PedidoService pedidoService;

    // Obtener pedidos por ID de comprador
    @GetMapping("/pedidos/comprador/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<Pedido> obtenerPedidosPorComprador(@PathVariable Integer id) {
        return service.findPedidosByCompradorId(id);
    }

    // Obtener todos los pedidos
    @GetMapping("/pedidos")
    @ResponseStatus(HttpStatus.OK)
    public List<Pedido> readAll() {
        return service.readAll();
    }

    // Obtener un pedido por ID
    @GetMapping("/pedidos/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Pedido read(@PathVariable Integer id) {
        Pedido pedido = service.read(id);
        if (pedido == null) {
            throw new RuntimeException("Pedido no encontrado con ID: " + id);
        }
        return pedido;
    }

    @PostMapping("/pedidos")
    @ResponseStatus(HttpStatus.CREATED)
    public Pedido create(@RequestBody Pedido pedido) {
        validarPedido(pedido);

        // Extraer y desconectar los detalles antes de guardar el pedido
        List<DetallePedido> detalles = pedido.getDetalles();
        pedido.setDetalles(null); // evita guardar detalles con pedido null

        // 1. Guardar el pedido sin detalles (esto genera el no_orden)
        Pedido pedidoGuardado = service.save(pedido);

        // 2. Asociar el pedido ya guardado a cada detalle
        for (DetallePedido detalle : detalles) {
            detalle.setPedido(pedidoGuardado);
        }

        // 3. Asignar los detalles al pedido guardado
        pedidoGuardado.setDetalles(detalles);

        // 4. Volver a guardar para persistir los detalles (asumiendo cascada)
        return service.save(pedidoGuardado);
    }


    // Actualizar pedido
    @PutMapping("/pedidos/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Pedido update(@PathVariable Integer id, @RequestBody Pedido pedido) {
        Pedido p = service.read(id);
        if (p == null) {
            throw new RuntimeException("No se puede actualizar: Pedido con ID " + id + " no existe.");
        }

        validarPedido(pedido);

        p.setDescripcion_producto(pedido.getDescripcion_producto());
        p.setPago_final(pedido.getPago_final());
        p.setComprador(pedido.getComprador());
        p.setCafeteria(pedido.getCafeteria());

        return service.save(p);
    }

    // Eliminar pedido
    @DeleteMapping("/pedidos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        Pedido pedido = service.read(id);
        if (pedido == null) {
            throw new RuntimeException("No se puede eliminar: Pedido con ID " + id + " no existe.");
        }
        service.delete(id);
    }

    // Validaciones básicas
    private void validarPedido(Pedido pedido) {
        if (pedido.getDescripcion_producto() == null || pedido.getDescripcion_producto().isBlank()) {
            throw new RuntimeException("La descripción del producto es obligatoria.");
        }
        if (pedido.getPago_final() == null || pedido.getPago_final().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("El pago final debe ser mayor o igual a 0.");
        }
        if (pedido.getComprador() == null || pedido.getComprador().getId_Comprador() == null) {
            throw new RuntimeException("El pedido debe tener un comprador válido.");
        }
        if (pedido.getCafeteria() == null || pedido.getCafeteria().getIdCafeteria() == null) {
            throw new RuntimeException("El pedido debe estar asociado a una cafetería.");
        }
    }

    //PDF
    @GetMapping("/{id}/ticket")
    public ResponseEntity<InputStreamResource> generarPDF(@PathVariable Integer id) {
        Pedido pedido = pedidoService.findById(id);
        ByteArrayInputStream stream = pedidoService.reportePDF(pedido);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition","inline; filename=Pedido.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(stream));
    }
}
