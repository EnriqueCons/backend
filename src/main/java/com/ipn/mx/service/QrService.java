package com.ipn.mx.service;

import com.ipn.mx.domain.entity.QR;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface QrService {
    List<QR> readAll(); // sin throws
    QR read(Integer id); // sin throws
    QR save(QR qr); // sin throws
    QR save(MultipartFile file) throws IOException; // solo este lo necesita
    void delete(Integer id); // sin throws
}
