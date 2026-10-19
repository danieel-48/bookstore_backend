package co.edu.sena.auth.dto;

import co.edu.sena.auth.model.User;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Datos basicos del perfil de un usuario (nunca incluye la contrasena).
 */
@Schema(description = "Datos basicos del perfil de usuario")
public record ProfileResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "Ana Ariza") String name,
        @Schema(example = "ana12345@test.com") String email,
        @Schema(example = "EMPLOYEE") String role,
        @Schema(example = "true") boolean active) {

    /** Convierte la entidad {@link User} en su representacion de perfil. */
    public static ProfileResponse from(User u) {
        return new ProfileResponse(u.getId(), u.getName(), u.getEmail(), u.getRole(), u.isActive());
    }
}
