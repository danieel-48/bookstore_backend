package co.edu.sena.auth.controller;

import co.edu.sena.auth.dto.LoginRequest;
import co.edu.sena.auth.dto.MessageResponse;
import co.edu.sena.auth.dto.RegisterRequest;
import co.edu.sena.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints REST de autenticacion bajo /api/auth.
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticacion", description = "Registro e inicio de sesion")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    /** POST /api/auth/register - crea un usuario. Responde 201 sin datos del usuario. */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar usuario",
            description = "Crea una cuenta activa. Si no se envia role se asigna EMPLOYEE (valores: ADMIN o EMPLOYEE).")
    @ApiResponse(responseCode = "201", description = "Usuario registrado")
    @ApiResponse(responseCode = "400", description = "Datos invalidos o rol invalido", content = @Content)
    @ApiResponse(responseCode = "409", description = "El email ya esta registrado", content = @Content)
    public MessageResponse register(@Valid @RequestBody RegisterRequest request) {
        service.register(request);
        return MessageResponse.ok("Usuario registrado");
    }

    /** POST /api/auth/login - valida credenciales; si son correctas responde 200 sin datos del usuario. */
    @PostMapping("/login")
    @Operation(summary = "Iniciar sesion", description = "Valida correo y contrasena. No devuelve datos del usuario.")
    @ApiResponse(responseCode = "200", description = "Login exitoso")
    @ApiResponse(responseCode = "400", description = "Datos invalidos", content = @Content)
    @ApiResponse(responseCode = "401", description = "Credenciales incorrectas", content = @Content)
    @ApiResponse(responseCode = "403", description = "Usuario inactivo", content = @Content)
    public MessageResponse login(@Valid @RequestBody LoginRequest request) {
        service.login(request);
        return MessageResponse.ok("Login exitoso");
    }
}
