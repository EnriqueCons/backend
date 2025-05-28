package com.ipn.mx.service.impl;

import com.ipn.mx.domain.entity.QR;
import com.ipn.mx.domain.repository.QrRepository;
import com.ipn.mx.service.QrService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class QrServiceImpl implements QrService {
    @Autowired
    private QrRepository qrRepository;

    //private final String URL_ARCHIVO = "/Users/enriquecontreras/Pictures/imagenes";

    @Override
    @Transactional(readOnly = true)
    public List<QR> readAll() {

        return qrRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public QR read(Integer id) {
          return qrRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public QR save(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío");
        }

        QR qr = QR.builder()
                .nombreArchivo(file.getOriginalFilename())
                .tipoArchivo(file.getContentType())
                .datosArchivo(file.getBytes())
                .build();

        QR savedQr = qrRepository.save(qr);
        log.info("QR guardado: {}", qr.getNombreArchivo());

        return savedQr;
    }

    @Override
    @Transactional
    public QR save(QR qr) {
        return qrRepository.save(qr);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        qrRepository.deleteById(id);
    }

}
