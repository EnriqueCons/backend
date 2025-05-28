package com.ipn.mx.infrastructure;

import com.ipn.mx.domain.entity.Comprador;
import com.ipn.mx.domain.entity.QR;
import com.ipn.mx.service.CompradorService;
import com.ipn.mx.service.QrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;

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

    // Crear QR con datos + archivo
    @PostMapping(value = "/qr/full", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> createQrWithFileAndData(
            @RequestParam("file") MultipartFile file,
            @RequestParam("cantidadPuntos") int cantidadPuntos,
            @RequestParam("caducidad") String caducidad,
            @RequestParam("idComprador") Comprador idComprador
    ) throws IOException {

        String respuesta = service.save(file, cantidadPuntos, caducidad, idComprador);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PutMapping("/qr/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public QR update(
            @PathVariable Integer id,
            @RequestParam("cantidadPuntos") int cantidadPuntos,
            @RequestParam("caducidad") String caducidad
    ) {
        QR qr1 = service.read(id);
        if (qr1 == null) {
            throw new RuntimeException("QR no encontrado con ID: " + id);
        }
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
