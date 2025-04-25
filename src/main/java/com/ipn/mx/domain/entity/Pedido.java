package com.ipn.mx.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
    private Double pago_final;

    // FOREIGN KEY: id_Comprador
    // FOREIGN KEY: id_cafeteria
}
