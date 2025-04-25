package com.ipn.mx.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
@Table(name = "Reseña")
public class Reseña implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resena", nullable = false)
    private Integer idResena;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "comentario", columnDefinition = "TEXT")
    private String comentario;

    @Column(name = "calificacion", nullable = false)
    @Min(value = 1, message = "La calificación mínima es 1")
    @Max(value = 5, message = "La calificación máxima es 5")
    private Integer calificacion;

    // FOREIGN KEY: id_comprador
    // FOREIGN KEY: no_orden

}
