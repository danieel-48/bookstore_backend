package co.edu.sena.auth.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Datos editables del perfil. Contrasena y rol son opcionales.
 */
@Schema(description = "Nuevo nombre y correo del usuario")
public record UpdateProfileRequest(
        @JsonAlias("Name") @NotBlank @Size(max = 150)
        @Schema(example = "Ana Ariza") String name,
        @JsonAlias("Email") @NotBlank @Email @Size(max = 100)
        @Schema(example = "ana12345@test.com") String email,
        @JsonAlias("Password") @Size(min = 6, max = 72, message = "La contraseña debe tener al menos 6 caracteres")
        @Schema(description = "Opcional; al enviarla se guarda con BCrypt (mínimo 6 caracteres)", example = "NuevaClave123")
        String password,
        @JsonAlias("Role")
        @Schema(description = "Opcional; valores permitidos: ADMIN o EMPLOYEE", example = "EMPLOYEE")
        String role) {
}
