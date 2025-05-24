package com.ipn.mx.service;

import com.ipn.mx.domain.entity.Cafeteria;

import java.util.List;

public interface CafeteriaService {
    public List<Cafeteria> readAll();
    public Cafeteria read(Integer id);
    public Cafeteria save(Cafeteria cafeteria);
    public void delete(Integer id);
}
