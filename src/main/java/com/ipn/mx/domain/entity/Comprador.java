package com.ipn.mx.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
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
@Table(name = "Comprador")
public class Comprador implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comprador_seq")
    @SequenceGenerator(name = "comprador_seq", sequenceName = "comprador_seq", allocationSize = 1)
    @Column(name = "id_Comprador", nullable = false)
    private Integer id_Comprador;

    @Column(name = "nombre", length = 50 , nullable = false)
    private String nombre;

    @Column(name = "apellidoPaterno", length = 50, nullable = false)
    private String apellidoPaterno;

    @Column(name = "apellidoMaterno", length = 50, nullable = true)
    private String apellidoMaterno;

    @Column(name = "correo", length = 100, nullable = false, unique = true)
    @Email(message = "El e-mail debe ser un e-mail válido")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@(alumno\\.ipn\\.mx|gmail\\.com)$", message = "El e-mail debe cumplir con la expresión regular")
    private String correo;

    @Column(name = "contrasenia", length = 100, nullable = false)
    private String contrasenia;

    @Column(name = "token_recuperacion", length = 100)
    private String tokenRecuperacion;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_expiracion_token")
    private Date fechaExpiracionToken;


}
