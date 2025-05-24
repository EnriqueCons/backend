package com.ipn.mx.service.impl;

import com.ipn.mx.domain.entity.Cafeteria;
import com.ipn.mx.domain.repository.CafeteriaRepository;
import com.ipn.mx.service.CafeteriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CafeteriaServiceImpl implements CafeteriaService {
    @Autowired
    private CafeteriaRepository cafeteriaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Cafeteria> readAll(){ return cafeteriaRepository.findAll(); }

    @Override
    @Transactional(readOnly = true)
    public Cafeteria read(Integer id) { return cafeteriaRepository.findById(id).orElse(null); }

    @Override
    @Transactional
    public Cafeteria save(Cafeteria cafeteria){ return cafeteriaRepository.save(cafeteria); }

    @Override
    @Transactional
    public void delete(Integer id) { this.cafeteriaRepository.deleteById(id); }
}
