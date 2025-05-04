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
@Table(name = "Menu")
public class Menu implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto", nullable = false)
    private Integer id_producto;

    @Column(name = "nombreProducto", length = 100, nullable = false)
    private String nombreProducto;

    @Column(name = "precio", precision = 6, scale = 2, nullable = false)
    private BigDecimal precio;

    @Column(name = "stock")
    private int stock;

    @ManyToOne
    @JoinColumn(name = "id_cafeteria", nullable = false)
    private Cafeteria cafeteria;


}
