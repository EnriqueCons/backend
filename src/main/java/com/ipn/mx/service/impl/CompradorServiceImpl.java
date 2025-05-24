package com.ipn.mx.service.impl;

import com.ipn.mx.domain.entity.Comprador;
import com.ipn.mx.domain.repository.CompradorRepository;
import com.ipn.mx.service.CompradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CompradorServiceImpl implements CompradorService {
    @Autowired
    private CompradorRepository compradorRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Comprador> readAll(){
        return compradorRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Comprador read(Integer id){
        return compradorRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Comprador save(Comprador comprador){
        return compradorRepository.save(comprador);
    }

    @Override
    @Transactional
    public void delete(Integer id){
        compradorRepository.deleteById(id);
    }
}
