package com.ipn.mx.service.impl;

import com.ipn.mx.domain.entity.Comprador;
import com.ipn.mx.domain.entity.QR;
import com.ipn.mx.domain.repository.CompradorRepository;
import com.ipn.mx.domain.repository.QrRepository;
import com.ipn.mx.service.CompradorService;
import com.ipn.mx.service.QrService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.nio.file.Files;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class QrServiceImpl implements QrService {
    @Autowired
    private QrRepository qrRepository;

    @Autowired
    private CompradorService compradorService;

    private final String URL_ARCHIVO = "/Users/enriquecontreras/Pictures/imagenes";

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
    public String save(MultipartFile file, int cantidadPuntos, String caducidad, Comprador idComprador) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío");
        }

        String ruta_archivo = URL_ARCHIVO + file.getOriginalFilename();
        QR qr = QR.
                builder()
                .nombreArchivo(file.getOriginalFilename())
                .tipoArchivo(file.getContentType())
                .datosArchivo(file.getBytes())
                .urlArchivo(ruta_archivo)
                .cantidadPuntos(cantidadPuntos)
                .caducidad(LocalDate.parse(caducidad))
                .comprador(idComprador)
                .build();

        qrRepository.save(qr);
        file.transferTo(new File(ruta_archivo));
        if (qr.getId_QR() != null) {
            return "Archivo almacenado satisfactoriamente";
        }
        return null;
    }

    @Override
    @Transactional
    public QR save(QR qr) {
        return qrRepository.save(qr);
    }

    @Override
    @Transactional
    public QR update(Integer id, int nuevaCantidadPuntos, String nuevaCaducidad) {
        QR qrExistente = qrRepository.findById(id).orElse(null);
        if (qrExistente == null) {
            throw new RuntimeException("QR con ID " + id + " no encontrado.");
        }

        LocalDate fecha;
        try {
            fecha = LocalDate.parse(nuevaCaducidad);
        } catch (Exception e) {
            throw new IllegalArgumentException("Formato de fecha inválido. Usa yyyy-MM-dd");
        }

        qrExistente.setCantidadPuntos(nuevaCantidadPuntos);
        qrExistente.setCaducidad(fecha);

        return qrRepository.save(qrExistente);
    }


    @Override
    @Transactional
    public void delete(Integer id) {
        qrRepository.deleteById(id);
    }

}
