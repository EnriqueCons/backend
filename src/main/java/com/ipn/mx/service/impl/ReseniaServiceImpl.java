package com.ipn.mx.service.impl;

import com.ipn.mx.domain.entity.Resenia;
import com.ipn.mx.domain.repository.ReseniaRepository;
import com.ipn.mx.service.ReseniaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReseniaServiceImpl implements ReseniaService {
    @Autowired
    private ReseniaRepository reseniaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Resenia> readAll() {
        return reseniaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Resenia read(Integer id) {
        return reseniaRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Resenia save(Resenia resenia){
        return reseniaRepository.save(resenia);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        reseniaRepository.deleteById(id);
    }
}
