package co.edu.sena.auth.controller;

import co.edu.sena.auth.dto.ProfileResponse;
import co.edu.sena.auth.dto.UpdateProfileRequest;
import co.edu.sena.auth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoints REST de usuarios bajo /api/users.
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuarios", description = "Consulta y actualizacion de usuarios")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    /** GET /api/users - lista todos los usuarios. */
    @GetMapping
    @Operation(
            summary = "Listar usuarios",
            description = """
                    ## Listar usuarios
                    Descripción: lista todos los usuarios registrados sin contraseña (HU-03)
                    Método y URL: GET /api/users
                    Body (JSON):
                    No aplica
                    Respuestas:
                    200 OK → lista de usuarios registrados
                    """
    )
    @ApiResponse(responseCode = "200", description = "Lista de usuarios registrados",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ProfileResponse.class))))
    public List<ProfileResponse> list() {
        return service.list();
    }

    /** GET /api/users/{email} - devuelve el perfil del usuario con ese email en el path. */
    @GetMapping("/{email:.+}")
    @Operation(
            summary = "Consultar perfil por email",
            description = """
                    ## Consultar perfil por email
                    Descripción: consulta el perfil de un usuario a partir de su correo en la ruta (HU-04)
                    Método y URL: GET /api/users/{email}
                    Body (JSON):
                    No aplica
                    Respuestas:
                    200 OK → perfil de usuario encontrado
                    400 Bad Request → email ausente o formato inválido
                    404 Not Found → usuario no encontrado
                    """
    )
    @ApiResponse(responseCode = "200", description = "Perfil de usuario encontrado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProfileResponse.class)))
    @ApiResponse(responseCode = "400", description = "Email ausente o formato inválido", content = @Content)
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    public ProfileResponse getByEmail(
            @Parameter(description = "Email del usuario", example = "ana12345@test.com")
            @PathVariable("email") @NotBlank @Email String email) {
        return service.getProfile(email);
    }

    /** GET /api/users/profile - devuelve el perfil del usuario con ese email por query param. */
    @GetMapping("/profile")
    @Operation(
            summary = "Consultar perfil por query param",
            description = """
                    ## Consultar perfil por query param
                    Descripción: consulta el perfil de un usuario a partir del parámetro email (HU-04)
                    Método y URL: GET /api/users/profile?email=ana12345@test.com
                    Body (JSON):
                    No aplica
                    Respuestas:
                    200 OK → perfil de usuario encontrado
                    400 Bad Request → email ausente o con formato inválido
                    404 Not Found → usuario no encontrado
                    """
    )
    @ApiResponse(responseCode = "200", description = "Perfil de usuario encontrado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProfileResponse.class)))
    @ApiResponse(responseCode = "400", description = "Email ausente o con formato inválido", content = @Content)
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    public ProfileResponse profile(
            @Parameter(description = "Email del usuario", example = "ana12345@test.com")
            @RequestParam("email") @NotBlank @Email String email) {
        return service.getProfile(email);
    }

    /** PUT /api/users/{id} - actualiza el perfil y devuelve los datos publicos actualizados. */
    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar usuario",
            description = """
                    ## Actualizar usuario
                    Descripción: actualiza nombre, correo y opcionalmente rol o contraseña (HU-05)
                    Método y URL: PUT /api/users/{id}
                    Body (JSON):
                    {
                      "name": "Ana Ariza",
                      "email": "ana12345@test.com",
                      "password": "123456",
                      "role": "ADMIN"
                    }
                    Respuestas:
                    200 OK → perfil actualizado exitosamente
                    400 Bad Request → datos inválidos o contraseña menor a 6 caracteres
                    404 Not Found → usuario no encontrado
                    409 Conflict → el correo ya está registrado por otro usuario
                    """
    )
    @ApiResponse(responseCode = "200", description = "Perfil actualizado exitosamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProfileResponse.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos o contraseña menor a 6 caracteres", content = @Content)
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    @ApiResponse(responseCode = "409", description = "El correo ya está registrado por otro usuario", content = @Content)
    public ProfileResponse updateProfile(
            @Parameter(description = "Id del usuario", example = "1") @PathVariable("id") Long id,
            @Valid @RequestBody UpdateProfileRequest request) {
        return service.updateProfile(id, request);
    }
}
