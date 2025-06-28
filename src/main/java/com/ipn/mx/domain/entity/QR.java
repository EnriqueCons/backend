package com.ipn.mx.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "QR")
public class QR implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qr_seq")
    @SequenceGenerator(name = "qr_seq", sequenceName = "qr_seq", allocationSize = 1)
    @Column(name = "id_QR", nullable = false)
    private Integer id_QR;

    @Column(name = "cantidadPuntos", nullable = false)
    private int cantidadPuntos;

    @Column(name = "caducidad")
    private LocalDate caducidad;

    @ManyToOne
    @JoinColumn(name = "id_Comprador", nullable = false)
    private Comprador comprador;
}