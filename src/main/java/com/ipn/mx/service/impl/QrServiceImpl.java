package com.ipn.mx.service.impl;

import com.ipn.mx.domain.entity.QR;
import com.ipn.mx.domain.repository.QrRepository;
import com.ipn.mx.service.QrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QrServiceImpl implements QrService {
    @Autowired
    private QrRepository qrRepository;

    @Override
    @Transactional(readOnly = true)
    public List<QR> readAll(){
        return qrRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public QR read(Integer id){
          return qrRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public QR save(QR qr){
        return qrRepository.save(qr);
    }

    @Override
    @Transactional
    public void delete(Integer id){
        qrRepository.deleteById(id);
    }

}
