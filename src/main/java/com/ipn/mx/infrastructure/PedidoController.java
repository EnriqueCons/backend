package com.ipn.mx.infrastructure;

import com.ipn.mx.domain.entity.Pedido;
import com.ipn.mx.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/apiPedido")
public class PedidoController {
    @Autowired
    private PedidoService service;

    @GetMapping("/pedidos")
    @ResponseStatus(HttpStatus.OK)
    public List<Pedido> readAll() {
        return service.readAll();
    }

    @GetMapping("/pedidos/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Pedido read(@PathVariable Integer id) {
        return service.read(id);
    }

    @PostMapping("/pedidos")
    @ResponseStatus(HttpStatus.CREATED)
    public Pedido create(@RequestBody Pedido pedido) {
        return service.save(pedido);
    }

    @PutMapping("/pedidos/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Pedido update(@PathVariable Integer id, @RequestBody Pedido pedido) {
        Pedido p = service.read(id);
        p.setDescripcion_producto(pedido.getDescripcion_producto());
        p.setPago_final(pedido.getPago_final());
        p.setComprador(pedido.getComprador());
        //p.setNo_orden(pedido.getNo_orden());
        p.setCafeteria(pedido.getCafeteria());
        return service.save(p);
    }

    @DeleteMapping("/pedidos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}
