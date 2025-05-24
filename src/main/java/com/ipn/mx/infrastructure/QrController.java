package com.ipn.mx.infrastructure;

import com.ipn.mx.domain.entity.QR;
import com.ipn.mx.service.QrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/apiQr")
public class QrController {
    @Autowired
    private QrService service;

    @GetMapping("/qr")
    @ResponseStatus(HttpStatus.OK)
    public List<QR> readAll(){
        return service.readAll();
    }

    @GetMapping("/qr/{id}")
    @ResponseStatus(HttpStatus.OK)
    public QR read(@PathVariable Integer id){
        return service.read(id);
    }

    @PostMapping("/qr")
    @ResponseStatus(HttpStatus.CREATED)
    public QR create(@RequestBody QR qr){
        return service.save(qr);
    }

    @PutMapping("/qr/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public QR update(@PathVariable Integer id, @RequestBody QR qr){
        QR qr1 = service.read(id);
        qr1.setImagen(qr.getImagen());
        //qr1.setId_QR(qr.getId_QR());
        qr1.setCantidadPuntos(qr.getCantidadPuntos());
        qr1.setCaducidad(qr.getCaducidad());
        qr1.setComprador(qr.getComprador());
        return service.save(qr1);
    }

    @DeleteMapping("/qr/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id){
        service.delete(id);
    }
}
