package co.edu.sena.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Datos para crear o actualizar un cliente (tabla clients). Todos los campos son obligatorios.
 */
@Schema(description = "Datos para crear o actualizar un cliente")
public record ClientRequest(
        @Schema(description = "Documento de identidad (unico, obligatorio)", example = "1012345678")
        @NotNull(message = "El campo 'idNumber' no debe estar vacío")
        @Positive(message = "El campo 'idNumber' debe ser un número positivo")
        Long idNumber,

        @Schema(description = "Nombre completo (obligatorio)", example = "Ana Ariza")
        @NotBlank(message = "El campo 'name' no debe estar vacío")
        @Size(max = 150, message = "El campo 'name' no debe superar los 150 caracteres")
        String name,

        @Schema(description = "Correo (unico, obligatorio)", example = "ana12345@test.com")
        @NotBlank(message = "El campo 'email' no debe estar vacío")
        @Email(message = "El campo 'email' debe tener un formato válido")
        @Size(max = 50, message = "El campo 'email' no debe superar los 50 caracteres")
        String email,

        @Schema(description = "Telefono (obligatorio)", example = "3001234567")
        @NotNull(message = "El campo 'phone' no debe estar vacío")
        @Positive(message = "El campo 'phone' debe ser un número positivo")
        Long phone) {
}
