package com.ipn.mx.infrastructure;

import com.ipn.mx.domain.entity.Comprador;
import com.ipn.mx.domain.repository.CompradorRepository;
import com.ipn.mx.service.CompradorService;
import com.ipn.mx.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/apiComprador")
public class CompradorController {
    private static final Logger logger = LoggerFactory.getLogger(CompradorController.class);

    @Autowired
    private CompradorService service;

    @Autowired
    private CompradorRepository compradorRepository;

    @Autowired
    private EmailService emailService;

    // Enviar correo para recuperar la contraseña
    @PostMapping("/comprador/recuperar")
    @ResponseStatus(HttpStatus.OK)
    public String recuperarContrasena(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        logger.info("Correo recibido para recuperación: {}", email);

        if (email == null || email.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo es requerido");
        }

        Optional<Comprador> opt = compradorRepository.findByCorreo(email);
        if (opt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Correo no registrado");
        }

        Comprador comprador = opt.get();
        String token = generarTokenRecuperacion();
        comprador.setTokenRecuperacion(token);
        comprador.setFechaExpiracionToken(calcularFechaExpiracion());
        compradorRepository.save(comprador);

        String enlace = "https://backend-o9xo.onrender.com/restablecer-contrasena?token=" + token;
        String cuerpo = "<p>Haz clic en el siguiente enlace para restablecer tu contraseña:</p>"
                + "<a href=\"" + enlace + "\">Restablecer contraseña</a>"
                + "<p>El enlace expirará en 24 horas.</p>";

        emailService.enviarCorreo(email, "Recuperación de contraseña", cuerpo);

        return "Se ha enviado un enlace de recuperación a tu correo";
    }

    private String generarTokenRecuperacion() {
        return UUID.randomUUID().toString();
    }

    private Date calcularFechaExpiracion() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 24);
        return calendar.getTime();
    }



    //Validar el token
    @GetMapping("/comprador/validar-token")
    @ResponseStatus(HttpStatus.OK)
    public boolean validarToken(@RequestParam("token") String token) {
        return service.validarToken(token);
    }

    //Restablecer la nueva contraseña
    @PostMapping("/comprador/restablecer")
    @ResponseStatus(HttpStatus.OK)
    public String restablecerContrasena(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String nuevaContrasena = request.get("nuevaContrasena");

        if (service.validarToken(token)) {
            service.actualizarContrasena(token, nuevaContrasena);
            return "Contraseña actualizada con éxito.";
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token inválido o expirado.");
        }
    }



    // Obtener todos los compradores
    @GetMapping("/compradores")
    @ResponseStatus(HttpStatus.OK)
    public List<Comprador> readAll() {
        return service.readAll();
    }

    // Obtener un comprador por ID
    @GetMapping("/compradores/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Comprador read(@PathVariable Integer id) {
        Comprador comprador = service.read(id);
        if (comprador == null) {
            throw new RuntimeException("Comprador no encontrado con ID: " + id);
        }
        return comprador;
    }

    // Crear nuevo comprador
    @PostMapping("/compradores")
    @ResponseStatus(HttpStatus.CREATED)
    public Comprador create(@RequestBody Comprador comprador) {
        validarComprador(comprador);
        return service.save(comprador);
    }

    // Actualizar comprador existente
    @PutMapping("/compradores/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Comprador update(@PathVariable Integer id, @RequestBody Comprador comprador) {
        Comprador comp = service.read(id);
        if (comp == null) {
            throw new RuntimeException("No se puede actualizar: Comprador con ID " + id + " no existe.");
        }

        validarComprador(comprador);

        comp.setNombre(comprador.getNombre());
        comp.setApellidoPaterno(comprador.getApellidoPaterno());
        comp.setApellidoMaterno(comprador.getApellidoMaterno());
        comp.setCorreo(comprador.getCorreo());
        comp.setContrasenia(comprador.getContrasenia());

        return service.save(comp);
    }

    // Eliminar comprador
    @DeleteMapping("/compradores/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        Comprador comp = service.read(id);
        if (comp == null) {
            throw new RuntimeException("No se puede eliminar: Comprador con ID " + id + " no existe.");
        }
        service.delete(id);
    }

    // Validación reutilizable
    private void validarComprador(Comprador c) {
        if (c.getNombre() == null || c.getNombre().isBlank()) {
            throw new RuntimeException("El nombre es obligatorio.");
        }
        if (c.getApellidoPaterno() == null || c.getApellidoPaterno().isBlank()) {
            throw new RuntimeException("El apellido paterno es obligatorio.");
        }
        if (c.getCorreo() == null || c.getCorreo().isBlank()) {
            throw new RuntimeException("El correo es obligatorio.");
        }
        if (c.getContrasenia() == null || c.getContrasenia().isBlank()) {
            throw new RuntimeException("La contraseña es obligatoria.");
        }
    }
}
