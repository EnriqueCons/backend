package com.ipn.mx.service;

import com.ipn.mx.domain.entity.Comprador;
import com.ipn.mx.domain.entity.QR;

import java.io.IOException;
import java.util.List;

public interface QrService {
    List<QR> readAll();
    QR read(Integer id);
    QR save(QR qr);
    String save(int cantidadPuntos, String caducidad, Comprador idComprador) throws IOException;
    QR update(Integer idQr, int nuevaCantidadPuntos, String nuevaCaducidad);
    void delete(Integer id);

    // Métodos para puntos
    void agregarPuntos(Comprador comprador, int puntos);
    void restarPuntos(Comprador comprador, int puntos);
    QR findByComprador(Comprador comprador);
}