package com.ipn.mx.service.impl;

import com.ipn.mx.domain.entity.Comprador;
import com.ipn.mx.domain.entity.QR;
import com.ipn.mx.domain.repository.QrRepository;
import com.ipn.mx.service.CompradorService;
import com.ipn.mx.service.QrService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class QrServiceImpl implements QrService {
    @Autowired
    private QrRepository qrRepository;

    @Autowired
    private CompradorService compradorService;

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
    public String save(int cantidadPuntos, String caducidad, Comprador idComprador) {
        QR qr = QR.builder()
                .cantidadPuntos(cantidadPuntos)
                .caducidad(LocalDate.parse(caducidad))
                .comprador(idComprador)
                .build();

        qrRepository.save(qr);
        if (qr.getId_QR() != null) {
            return "QR almacenado satisfactoriamente";
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

    // --- Métodos para puntos ---

    @Override
    @Transactional
    public void agregarPuntos(Comprador comprador, int puntos) {
        QR qr = qrRepository.findByComprador(comprador).orElse(null);
        if (qr == null) {
            qr = QR.builder().comprador(comprador).cantidadPuntos(puntos).build();
        } else {
            qr.setCantidadPuntos(qr.getCantidadPuntos() + puntos);
        }
        qrRepository.save(qr);
    }

    @Override
    @Transactional
    public void restarPuntos(Comprador comprador, int puntos) {
        QR qr = qrRepository.findByComprador(comprador).orElse(null);
        if (qr != null && qr.getCantidadPuntos() >= puntos) {
            qr.setCantidadPuntos(qr.getCantidadPuntos() - puntos);
            qrRepository.save(qr);
        } else {
            throw new RuntimeException("No tiene suficientes puntos.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public QR findByComprador(Comprador comprador) {
        return qrRepository.findByComprador(comprador).orElse(null);
    }
}