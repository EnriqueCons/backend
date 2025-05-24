package com.ipn.mx.service.impl;

import com.ipn.mx.domain.entity.Recompensa;
import com.ipn.mx.domain.repository.RecompensaRepository;
import com.ipn.mx.service.RecompensaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RecompensaServiceImpl implements RecompensaService {
    @Autowired
    private RecompensaRepository recompensaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Recompensa> readAll(){
        return recompensaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Recompensa read(Integer id){
        return recompensaRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Recompensa save(Recompensa recompensa){
        return recompensaRepository.save(recompensa);
    }

    @Override
    @Transactional
    public void delete(Integer id){
        recompensaRepository.deleteById(id);
    }
}
