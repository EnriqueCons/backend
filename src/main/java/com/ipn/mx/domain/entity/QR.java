package com.ipn.mx.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "QR")
public class QR implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_QR", nullable = false)
    private Integer id_QR;

    @Column(name = "imagen", columnDefinition = "TEXT")
    private String imagen;

    @Column(name = "cantidadPuntos", nullable = false)
    private int cantidadPuntos;

    @Column(name = "caducidad")
    private Date caducidad;

    //FOREIGN KEY: id_Comprador
}
