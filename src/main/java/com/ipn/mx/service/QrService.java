package com.ipn.mx.service;

import com.ipn.mx.domain.entity.Comprador;
import com.ipn.mx.domain.entity.QR;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface QrService {
    List<QR> readAll();
    QR read(Integer id);
    QR save(QR qr);
    String save(MultipartFile file, int cantidadPuntos, String caducidad, Comprador idComprador) throws IOException;
    QR update(Integer idQr, int nuevaCantidadPuntos, String nuevaCaducidad);
    void delete(Integer id);
}
