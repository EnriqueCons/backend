package com.ipn.mx.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String tipo; // "cafeteria" o "comprador"
    private Integer id;
    private String nombre;
}
