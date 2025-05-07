package com.ipn.mx.service;

import com.ipn.mx.domain.entity.Resenia;

import java.util.List;

public interface ReseniaService {
    public List<Resenia> readAll();
    public Resenia read(Integer id);
    public Resenia save(Resenia resenia);
    public void delete(Integer id);
}
