package com.ipn.mx.domain.entity;

import lombok.Data;

@Data
public class LoginRequest {
    private String correo;
    private String contrasenia;
}
