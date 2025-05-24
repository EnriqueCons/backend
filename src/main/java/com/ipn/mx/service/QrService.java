package com.ipn.mx.service;

import com.ipn.mx.domain.entity.QR;

import java.util.List;

public interface QrService {
    public List<QR> readAll();
    public QR read(Integer id);
    public QR save(QR Qr);
    public void delete(Integer id);

}
