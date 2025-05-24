package com.ipn.mx.service;

import com.ipn.mx.domain.entity.Pedido;

import java.util.List;

public interface PedidoService {
    public List<Pedido> readAll();
    public Pedido read(Integer id);
    public Pedido save(Pedido pedido);
    public void delete(Integer id);

}
