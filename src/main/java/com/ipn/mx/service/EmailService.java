package com.ipn.mx.service;

public interface EmailService {
    void enviarCorreo(String destinatario, String asunto, String cuerpoHtml);
}