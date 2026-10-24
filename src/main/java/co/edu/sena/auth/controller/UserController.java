package co.edu.sena.auth.controller;

import co.edu.sena.auth.dto.ProfileResponse;
import co.edu.sena.auth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints REST de usuarios bajo /api/users.
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuarios", description = "Consulta de perfil de usuario")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    /** GET /api/users/profile - devuelve el perfil del usuario con ese email. */
    @GetMapping("/profile")
    @Operation(summary = "Consultar perfil", description = "Devuelve los datos basicos del usuario (sin contrasena).")
    @ApiResponse(responseCode = "200", description = "Perfil encontrado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProfileResponse.class)))
    @ApiResponse(responseCode = "400", description = "Email ausente o con formato invalido", content = @Content)
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    public ProfileResponse profile(
            @Parameter(description = "Email del usuario", example = "ana12345@test.com")
            @RequestParam("email") @NotBlank @Email String email) {
        return service.getProfile(email);
    }
}
