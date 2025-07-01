package com.ipn.mx.service.impl;

import com.ipn.mx.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailServiceImpl implements EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void enviarCorreo(String destinatario, String asunto, String cuerpoHtml) {
        try {
            logger.info("Intentando enviar correo a: {}", destinatario);

            if (destinatario == null || destinatario.isBlank()) {
                logger.error("El destinatario no puede ser nulo o vacío");
                throw new IllegalArgumentException("El destinatario no puede ser nulo o vacío");
            }

            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(cuerpoHtml, true);

            mailSender.send(mensaje);
            logger.info("Correo enviado exitosamente a: {}", destinatario);
        } catch (MessagingException e) {
            logger.error("Error al enviar el correo a {}: {}", destinatario, e.getMessage());
            throw new RuntimeException("Error al enviar el correo", e);
        }
    }
}