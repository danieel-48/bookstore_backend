package co.edu.sena.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Datos para crear o actualizar un cliente (tabla clients). Solo name es obligatorio.
 */
@Schema(description = "Datos para crear o actualizar un cliente")
public record ClientRequest(
        @Schema(description = "Documento de identidad (unico, opcional)", example = "1012345678") @Positive Long idNumber,
        @Schema(example = "Ana Ariza") @NotBlank @Size(max = 150) String name,
        @Schema(description = "Correo (unico, opcional)", example = "ana12345@test.com") @Email @Size(max = 50) String email,
        @Schema(description = "Telefono (opcional)", example = "3001234567") @Positive Long phone) {
}
