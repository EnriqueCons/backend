package com.ipn.mx.service.impl;

import com.ipn.mx.domain.entity.Cafeteria;
import com.ipn.mx.domain.entity.DetallePedido;
import com.ipn.mx.domain.entity.Menu;
import com.ipn.mx.domain.entity.Pedido;
import com.ipn.mx.domain.repository.PedidoRepository;
import com.ipn.mx.service.PedidoService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;

@Service
@Transactional
public class PedidoServiceImpl implements PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;

    @Override
    public List<Pedido> readAll() {
        return pedidoRepository.findAll();
    }

    @Override
    public Pedido read(Integer id) {
        return pedidoRepository.findById(id).orElse(null);
    }

    @Override
    public Pedido save(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    @Override
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
            Font fontTitulo = FontFactory.getFont(FontFactory.TIMES_BOLD, 18, new BaseColor(255, 100, 0));
            Paragraph titulo = new Paragraph("Ticket de Pedido", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(15f);
            documento.add(titulo);

            // Información de la cafetería
            Cafeteria cafeteria = pedido.getCafeteria();
            Paragraph cafeInfo = new Paragraph(
                    String.format("Cafetería: %s\nUbicación: %s\nHorario: %s - %s",
                            cafeteria.getNombre(),
                            cafeteria.getUbicacion(),
                            cafeteria.getHora_inicio(),
                            cafeteria.getHora_fin()
                    )
            );
            cafeInfo.setSpacingAfter(10f);
            documento.add(cafeInfo);

            // Información del pedido
            Paragraph pedidoInfo = new Paragraph(
                    String.format("Número de Orden: %d\nDescripción: %s\nFecha de Creación: %s",
                            pedido.getNo_orden(),
                            pedido.getDescripcion_producto(),
                            pedido.getFechaCreacion().toString()
                    )
            );
            pedidoInfo.setSpacingAfter(10f);
            documento.add(pedidoInfo);

            // Tabla de productos
            PdfPTable tabla = new PdfPTable(4);
            tabla.setWidthPercentage(100);
            tabla.setSpacingBefore(10f);
            tabla.setSpacingAfter(10f);
            tabla.setWidths(new float[]{4, 2, 2, 2});

            // Encabezados
            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            Stream.of("Producto", "Cantidad", "Precio Unitario", "Subtotal")
                    .forEach(col -> {
                        PdfPCell header = new PdfPCell(new Phrase(col, headFont));
                        header.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tabla.addCell(header);
                    });

            // Filas de productos
            for (DetallePedido detalle : pedido.getDetalles()) {
                Menu producto = detalle.getProducto();

                tabla.addCell(new PdfPCell(new Phrase(producto.getNombreProducto())));
                tabla.addCell(new PdfPCell(new Phrase(String.valueOf(detalle.getCantidad()))));
                tabla.addCell(new PdfPCell(new Phrase(String.format("$%.2f", producto.getPrecio()))));
                tabla.addCell(new PdfPCell(new Phrase(String.format("$%.2f", detalle.getSubtotal()))));
            }

            documento.add(tabla);

            // Total a pagar
            Paragraph total = new Paragraph("Total a pagar: $" + String.format("%.2f", pedido.getPago_final()));
            total.setAlignment(Element.ALIGN_RIGHT);
            total.setSpacingAfter(15f);
            documento.add(total);

            // Mensaje final
            Font fontGracias = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 12);
            Paragraph gracias = new Paragraph("¡Gracias por su compra!", fontGracias);
            gracias.setAlignment(Element.ALIGN_CENTER);
            documento.add(gracias);

        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            documento.close();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    @Override
    public List<Pedido> findPedidosByCafeteriaId(Integer idCafeteria) {
        return pedidoRepository.findByCafeteriaIdCafeteria(idCafeteria);
    }

    @Override
    public List<Pedido> findPedidosByCompradorId(Integer idComprador) {
        return pedidoRepository.findPedidosByCompradorId(idComprador);
    }
}