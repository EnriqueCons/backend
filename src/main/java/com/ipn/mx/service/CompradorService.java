package com.ipn.mx.service;

import com.ipn.mx.domain.entity.Comprador;

import java.util.List;

public interface CompradorService {
    public List<Comprador> readAll();
    public Comprador read(Integer id);
    public Comprador save(Comprador comprador);
    public void delete(Integer id);

}
