package com.ipn.mx.service.impl;

import com.ipn.mx.domain.entity.Pedido;
import com.ipn.mx.domain.repository.PedidoRepository;
import com.ipn.mx.service.PedidoService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PedidoServiceImpl implements PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> readAll() {

        return pedidoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Pedido read(Integer id) {

        return pedidoRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Pedido save(Pedido pedido) {

        return pedidoRepository.save(pedido);
    }

    @Override
    @Transactional
    public void delete(Integer id) {

        pedidoRepository.deleteById(id);
    }

    @Override
    public Pedido findById(Integer id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido NO encontrado, ID:" + id));
    }

    @Override
    public ByteArrayInputStream reportePDF(Pedido pedido) {
        Document documento = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(documento, out);
            documento.open();

            // Título
            Font tituloArchivo = FontFactory.getFont(FontFactory.TIMES, 18, new BaseColor(255,100,0));
            Paragraph titulo = new Paragraph("Ticket de Pedido", tituloArchivo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            documento.add(titulo);
            documento.add(Chunk.NEWLINE);

            // Información de la cafetería
            Paragraph cafeInfo = new Paragraph("Cafetería: " + pedido.getCafeteria().getNombre()
                    + "\nUbicación: " + pedido.getCafeteria().getUbicacion()
                    + "\nHorario: " + pedido.getCafeteria().getHora_inicio() + " - " + pedido.getCafeteria().getHora_fin());
            cafeInfo.setSpacingAfter(10f);
            documento.add(cafeInfo);

            // Información del comprador
            Paragraph compradorInfo = new Paragraph("Cliente: " + pedido.getComprador().getNombre()
                    + " " + pedido.getComprador().getApellidoPaterno()
                    + " " + pedido.getComprador().getApellidoMaterno()
                    + "\nCorreo: " + pedido.getComprador().getCorreo());
            compradorInfo.setSpacingAfter(10f);
            documento.add(compradorInfo);

            // Detalles del pedido
            Paragraph pedidoInfo = new Paragraph("Número de Orden: " + pedido.getNo_orden()
                    + "\nDescripción: " + pedido.getDescripcion_producto()
                    + "\nPago Final: $" + pedido.getPago_final());
            pedidoInfo.setSpacingAfter(15f);
            documento.add(pedidoInfo);

            Paragraph gracias = new Paragraph("¡Gracias por su compra!", FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 12));
            gracias.setAlignment(Element.ALIGN_CENTER);
            documento.add(gracias);

            documento.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    @Override
    public List<Pedido> findPedidosByCompradorId(Integer idComprador) {
        return pedidoRepository.findPedidosByCompradorId(idComprador);
    }

}
