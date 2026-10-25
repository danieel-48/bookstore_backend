package co.edu.sena.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Datos para crear o actualizar un libro del catalogo. Todos los campos son obligatorios.
 */
@Schema(description = "Datos del libro")
public record BookRequest(
        @Schema(example = "Cien anos de soledad") @NotBlank @Size(max = 250) String title,
        @Schema(example = "Gabriel Garcia Marquez") @NotBlank @Size(max = 100) String author,
        @Schema(example = "Novela") @NotBlank @Size(max = 100) String category,
        @Schema(example = "22.00") @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal price,
        @Schema(example = "10") @NotNull @Min(0) Integer stock) {
}
