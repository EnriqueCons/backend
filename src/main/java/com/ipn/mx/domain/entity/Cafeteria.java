package com.ipn.mx.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Date;

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

    @Column(name = "correo", length = 100, nullable = false, unique = true)
    @Email(message = "El e-mail debe ser un e-mail válido")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@(alumno\\.ipn\\.mx|gmail\\.com)$",
            message = "El e-mail debe cumplir con la expresión regular")
    private String correo;

    @Column(name = "contrasenia", length = 100, nullable = false)
    private String contrasenia;

    @Column(name = "token_recuperacion", length = 100)
    private String tokenRecuperacion;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_expiracion_token")
    private Date fechaExpiracionToken;

    @Column(name = "nombre_imagen")
    private String nombreImagen;

    @Column(name = "tipo_imagen")
    private String tipoImagen;

    @Column(name = "datos_imagen")
    private byte[] datosImagen;
}
