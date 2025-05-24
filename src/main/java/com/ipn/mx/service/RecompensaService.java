package com.ipn.mx.service;

import com.ipn.mx.domain.entity.Recompensa;

import java.util.List;

public interface RecompensaService {
    public List<Recompensa> readAll();
    public Recompensa read(Integer id);
    public Recompensa save(Recompensa recompensa);
    public void delete(Integer id);
}
