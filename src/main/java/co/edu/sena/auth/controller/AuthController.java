package co.edu.sena.auth.controller;

import co.edu.sena.auth.dto.LoginRequest;
import co.edu.sena.auth.dto.RegisterRequest;
import co.edu.sena.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Endpoints REST de autenticacion bajo /api/auth.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    /** POST /api/auth/register - crea un usuario. Responde 201 sin datos del usuario. */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> register(@Valid @RequestBody RegisterRequest request) {
        service.register(request);
        return Map.of("success", true, "message", "Usuario registrado");
    }

    /** POST /api/auth/login - valida credenciales; si son correctas responde 200 sin datos del usuario. */
    @PostMapping("/login")
    public Map<String, Object> login(@Valid @RequestBody LoginRequest request) {
        service.login(request);
        return Map.of("success", true, "message", "Login exitoso");
    }
}
