package com.ipn.mx.infrastructure;

import com.ipn.mx.domain.entity.Cafeteria;
import com.ipn.mx.domain.entity.Comprador;
import com.ipn.mx.domain.entity.DetallePedido;
import com.ipn.mx.domain.entity.Pedido;
import com.ipn.mx.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/apiPedidos")
public class PedidoController {

    @Autowired
    private PedidoService service;
    @Autowired
    private PedidoService pedidoService;

    // Obtener todos los pedidos
    @GetMapping("/pedido")
    @ResponseStatus(HttpStatus.OK)
    public List<Pedido> readAll() {
        return service.readAll();
    }

    // Obtener un pedido por ID
    @GetMapping("/pedido/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Pedido read(@PathVariable Integer id) {
        Pedido pedido = service.read(id);
        if (pedido == null) {
            throw new RuntimeException("Pedido no encontrado con ID: " + id);
        }
        return pedido;
    }

    @PostMapping("/pedido")
    @ResponseStatus(HttpStatus.CREATED)
    public Pedido create(@RequestBody Pedido pedido) {

        // Extraer y separar los detalles para evitar problemas con referencias circulares
        List<DetallePedido> detalles = pedido.getDetalles();
        pedido.setDetalles(null); // Evita guardar detalles con un pedido que aún no existe

        // Guardar el pedido sin detalles (esto genera el ID/no_orden)
        Pedido pedidoGuardado = service.save(pedido);

        // Asignar el pedido ya guardado a cada detalle
        for (DetallePedido detalle : detalles) {
            detalle.setPedido(pedidoGuardado);
        }

        // Asignar la lista de detalles al pedido guardado
        pedidoGuardado.setDetalles(detalles);

        // Guardar nuevamente para persistir los detalles (asumiendo que hay cascada en la relación)
        return service.save(pedidoGuardado);
    }



    // Actualizar un pedido existente
    @PutMapping("/pedido/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Pedido update(@PathVariable Integer id, @RequestBody Pedido pedido) {
        Pedido existente = service.read(id);
        if (existente == null) {
            throw new RuntimeException("No se puede actualizar: Pedido con ID " + id + " no existe.");
        }
        // Actualiza los campos necesarios
        existente.setDescripcion_producto(pedido.getDescripcion_producto());
        existente.setPago_final(pedido.getPago_final());
        existente.setCafeteria(pedido.getCafeteria());
        existente.setComprador(pedido.getComprador());
        existente.setDetalles(pedido.getDetalles());
        return service.save(existente);
    }

    // Eliminar un pedido
    @DeleteMapping("/pedido/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        Pedido pedido = service.read(id);
        if (pedido == null) {
            throw new RuntimeException("No se puede eliminar: Pedido con ID " + id + " no existe.");
        }
        service.delete(id);
    }

    // Generar y descargar el PDF del ticket del pedido
    @GetMapping("/pedido/{id}/ticket")
    public ResponseEntity<byte[]> descargarTicket(@PathVariable Integer id) {
        Pedido pedido = service.read(id);
        if (pedido == null) {
            throw new RuntimeException("Pedido no encontrado con ID: " + id);
        }
        ByteArrayInputStream pdfStream = service.reportePDF(pedido);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=ticket_pedido_" + id + ".pdf")
                .header("Content-Type", "application/pdf")
                .body(pdfStream.readAllBytes());
    }

    // Obtener pedidos por id de comprador
    @GetMapping("/pedido/comprador/{idComprador}")
    @ResponseStatus(HttpStatus.OK)
    public List<Pedido> findPedidosByCompradorId(@PathVariable Integer idComprador) {
        return service.findPedidosByCompradorId(idComprador);
    }
}