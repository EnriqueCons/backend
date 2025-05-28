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

import java.io.IOException;
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

    // Leer todos los QRs
    @GetMapping("/qr")
    @ResponseStatus(HttpStatus.OK)
    public List<QR> readAll() throws IOException {
        return service.readAll();
    }

    // Leer QR por ID
    @GetMapping("/qr/{id}")
    @ResponseStatus(HttpStatus.OK)
    public QR read(@PathVariable Integer id) throws IOException {
        QR qr = service.read(id);
        if (qr == null) {
            throw new RuntimeException("QR no encontrado con ID: " + id);
        }
        return qr;
    }

    // Crear QR con datos + archivo (un solo paso)
    @PostMapping(value = "/qr/full", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public QR createQrWithFileAndData(
            @RequestParam("file") MultipartFile file,
            @RequestParam("cantidadPuntos") int cantidadPuntos,
            @RequestParam("caducidad") String caducidad,
            @RequestParam("id_Comprador") Integer idComprador
    ) throws IOException {

        if (file.isEmpty()) {
            throw new RuntimeException("El archivo no puede estar vacío.");
        }

        Comprador comprador = compradorService.read(idComprador);
        if (comprador == null) {
            throw new RuntimeException("Comprador no encontrado con ID: " + idComprador);
        }

        QR qr = QR.builder()
                .nombreArchivo(file.getOriginalFilename())
                .tipoArchivo(file.getContentType())
                .datosArchivo(file.getBytes())
                .cantidadPuntos(cantidadPuntos)
                .caducidad(LocalDate.parse(caducidad))
                .comprador(comprador)
                .build();

        return service.save(qr);
    }

    // Obtener imagen binaria de QR
    @GetMapping("/qr/{id}/imagen")
    public ResponseEntity<byte[]> getQrImage(@PathVariable Integer id) throws IOException {
        QR qr = service.read(id);
        if (qr == null || qr.getDatosArchivo() == null) {
            return ResponseEntity.notFound().build();
        }

        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        try {
            mediaType = MediaType.parseMediaType(qr.getTipoArchivo());
        } catch (Exception ignored) {}

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(qr.getDatosArchivo());
    }

    // Actualizar solo datos de un QR (no la imagen)
    @PutMapping("/qr/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public QR update(@PathVariable Integer id, @RequestBody QR qr) throws IOException {
        QR qr1 = service.read(id);
        if (qr1 == null) {
            throw new RuntimeException("QR no encontrado con ID: " + id);
        }

        qr1.setCantidadPuntos(qr.getCantidadPuntos());
        qr1.setCaducidad(qr.getCaducidad());
        qr1.setComprador(qr.getComprador());

        return service.save(qr1);
    }

    // Eliminar QR
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
