package com.ipn.mx.service;

import com.ipn.mx.domain.entity.Cafeteria;
import com.ipn.mx.domain.entity.Pedido;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface PedidoService {
    public List<Pedido> readAll();
    public Pedido read(Integer id);
    public Pedido save(Pedido pedido);
    public void delete(Integer id);

    public Pedido findById(Integer id);
    ByteArrayInputStream reportePDF(Pedido pedido);
}
