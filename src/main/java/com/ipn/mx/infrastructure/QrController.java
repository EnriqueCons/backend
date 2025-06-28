package com.ipn.mx.infrastructure;

import com.ipn.mx.domain.entity.Comprador;
import com.ipn.mx.domain.entity.QR;
import com.ipn.mx.service.CompradorService;
import com.ipn.mx.service.QrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/apiQr")
public class QrController {

    @Autowired
    private QrService service;

    @Autowired
    private CompradorService compradorService;

    @GetMapping("/qr")
    @ResponseStatus(HttpStatus.OK)
    public List<QR> readAll() throws IOException {
        return service.readAll();
    }

    @GetMapping("/qr/{id}")
    @ResponseStatus(HttpStatus.OK)
    public QR read(@PathVariable Integer id) throws IOException {
        QR qr = service.read(id);
        if (qr == null) {
            throw new RuntimeException("QR no encontrado con ID: " + id);
        }
        return qr;
    }

    // Crear QR solo con datos (sin imagen)
    @PostMapping("/qr")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> createQr(@RequestBody Map<String, Object> payload) throws IOException {
        int cantidadPuntos = (int) payload.get("cantidadPuntos");
        String caducidad = (String) payload.get("caducidad");
        Integer idCompradorInt = (Integer) payload.get("idComprador");
        Comprador idComprador = compradorService.read(idCompradorInt);
        if (idComprador == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Comprador no encontrado");
        }
        String respuesta = service.save(cantidadPuntos, caducidad, idComprador);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PutMapping("/qr/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public QR update(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> payload
    ) {
        QR qr1 = service.read(id);
        if (qr1 == null) {
            throw new RuntimeException("QR no encontrado con ID: " + id);
        }
        int cantidadPuntos = (int) payload.get("cantidadPuntos");
        String caducidad = (String) payload.get("caducidad");
        qr1.setCantidadPuntos(cantidadPuntos);
        try {
            qr1.setCaducidad(LocalDate.parse(caducidad));
        } catch (Exception e) {
            throw new IllegalArgumentException("Formato de fecha inválido. Usa yyyy-MM-dd");
        }

        return service.save(qr1);
    }

    @DeleteMapping("/qr/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) throws IOException {
        QR qr = service.read(id);
        if (qr == null) {
            throw new RuntimeException("QR con ID " + id + " no existe.");
        }
        service.delete(id);
    }
}