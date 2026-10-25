package co.edu.sena.auth.controller;

import co.edu.sena.auth.dto.LoginRequest;
import co.edu.sena.auth.dto.ProfileResponse;
import co.edu.sena.auth.dto.RegisterRequest;
import co.edu.sena.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

    /** POST /api/auth/register - crea un usuario y devuelve su perfil publico. */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Registrar usuario",
            description = """
                    ## Registrar usuario
                    Descripción: registra un usuario nuevo en el sistema con rol EMPLOYEE o ADMIN (HU-01)
                    Método y URL: POST /api/auth/register
                    Body (JSON):
                    {
                      "name": "Ana Ariza",
                      "email": "ana12345@test.com",
                      "password": "123456",
                      "role": "EMPLOYEE"
                    }
                    Respuestas:
                    201 Created → usuario registrado exitosamente
                    400 Bad Request → datos inválidos, rol inválido o contraseña menor a 6 caracteres
                    409 Conflict → el correo ya se encuentra registrado
                    """
    )
    @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProfileResponse.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos, rol inválido o contraseña menor a 6 caracteres", content = @Content)
    @ApiResponse(responseCode = "409", description = "El correo ya se encuentra registrado", content = @Content)
    public ProfileResponse register(@Valid @RequestBody RegisterRequest request) {
        return service.register(request);
    }

    /** POST /api/auth/login - valida credenciales y devuelve el perfil publico del usuario. */
    @PostMapping("/login")
    @Operation(
            summary = "Iniciar sesión",
            description = """
                    ## Iniciar sesión
                    Descripción: valida credenciales y devuelve el perfil del usuario (HU-02)
                    Método y URL: POST /api/auth/login
                    Body (JSON):
                    {
                      "email": "ana12345@test.com",
                      "password": "123456"
                    }
                    Respuestas:
                    200 OK → login exitoso y datos del usuario
                    400 Bad Request → datos inválidos o vacíos
                    401 Unauthorized → credenciales incorrectas
                    403 Forbidden → usuario inactivo
                    """
    )
    @ApiResponse(responseCode = "200", description = "Login exitoso y datos del usuario",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProfileResponse.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos o vacíos", content = @Content)
    @ApiResponse(responseCode = "401", description = "Credenciales incorrectas", content = @Content)
    @ApiResponse(responseCode = "403", description = "Usuario inactivo", content = @Content)
    public ProfileResponse login(@Valid @RequestBody LoginRequest request) {
        return service.login(request);
    }
}
