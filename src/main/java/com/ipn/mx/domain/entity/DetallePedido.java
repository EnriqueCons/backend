package com.ipn.mx.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "DetallePedido")
public class DetallePedido implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "detalle_seq")
    @SequenceGenerator(name = "detalle_seq", sequenceName = "detalle_seq", allocationSize = 1)
    private Integer id_detalle;

    @ManyToOne
    @JoinColumn(name = "no_orden", nullable = false)
    @JsonBackReference
    private Pedido pedido;  // ✅ PROPIEDAD NECESARIA

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Menu producto;

    private Integer cantidad;

    private BigDecimal subtotal;
}
