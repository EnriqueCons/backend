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
@Table(name = "Recompensa")
public class Recompensa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recompensa_seq")
    @SequenceGenerator(name = "recompensa_seq", sequenceName = "recompensa_seq", allocationSize = 1)
    @Column(name = "id_recompensa", nullable = false)
    private Integer id_recompensa;

    @Column(name = "cantidadPuntos", nullable = false)
    private int cantidadPuntos;

    @Column(name = "cantidadCompra", nullable = false)
    private int cantidadCompra;

    @ManyToOne
    @JoinColumn(name = "id_cafeteria", nullable = false)
    private Cafeteria cafeteria;

}
