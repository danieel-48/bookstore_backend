package co.edu.sena.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

/**
 * Datos para registrar una venta (tabla sales). Todos los campos son obligatorios.
 */
@Schema(description = "Datos para crear una venta")
public record SaleRequest(
        @Schema(description = "Fecha de la venta (obligatoria)", example = "2026-10-25T01:40:00")
        @NotNull(message = "El campo 'date' no debe estar vacío")
        LocalDateTime date,

        @Schema(description = "Id del cliente (obligatorio)", example = "2")
        @NotNull(message = "El campo 'idClient' no debe estar vacío")
        @Positive(message = "El campo 'idClient' debe ser un número positivo")
        Long idClient,

        @Schema(description = "Usuario que registra la venta (obligatorio)", example = "5")
        @NotNull(message = "El campo 'idUser' no debe estar vacío")
        @Positive(message = "El campo 'idUser' debe ser un número positivo")
        Long idUser) {
}
