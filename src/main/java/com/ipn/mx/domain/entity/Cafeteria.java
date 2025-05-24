package com.ipn.mx.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Cafeteria")
public class Cafeteria implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cafeteria_seq")
    @SequenceGenerator(name = "cafeteria_seq", sequenceName = "cafeteria_seq", allocationSize = 1)
    @Column(name = "id_cafeteria", nullable = false)
    private Integer idCafeteria;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "ubicacion", columnDefinition = "TEXT")
    private String ubicacion;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime hora_inicio;

    @Column(name = "hora_fin", nullable = false)
    private LocalTime hora_fin;

}
