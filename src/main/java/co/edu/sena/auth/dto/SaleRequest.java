package co.edu.sena.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * Datos para registrar una venta (tabla sales). El total inicia en 0 y se acumula al agregar detalles.
 */
@Schema(description = "Datos para crear una venta")
public record SaleRequest(
        @Schema(description = "Fecha de la venta; si se omite se usa la fecha actual", example = "2026-10-25T01:40:00")
        LocalDateTime date,
        @Schema(example = "1") @NotNull Long idClient,
        @Schema(description = "Usuario que registra la venta (opcional)", example = "1") Long idUser) {
}
