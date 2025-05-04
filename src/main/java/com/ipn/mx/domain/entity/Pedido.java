package com.ipn.mx.domain.entity;

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
@Table(name = "Pedido")
public class Pedido implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no_orden", nullable = false)
    private Integer no_orden;

    @Column(name = "descripcion_producto", columnDefinition = "TEXT")
    private String descripcion_producto;

    @Column(name = "pago_final", precision = 6, scale = 2, nullable = false)
    private BigDecimal pago_final;

    @ManyToOne
    @JoinColumn(name = "id_Comprador", nullable = false)
    private Comprador comprador;

    @ManyToOne
    @JoinColumn(name = "id_cafeteria", nullable = false)
    private Cafeteria cafeteria;

}
