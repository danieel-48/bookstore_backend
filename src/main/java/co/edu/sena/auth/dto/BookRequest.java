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
        @Schema(example = "Cien anos de soledad")
        @NotBlank(message = "El campo 'title' no debe estar vacío")
        @Size(max = 250, message = "El campo 'title' no debe superar los 250 caracteres")
        String title,

        @Schema(example = "Gabriel Garcia Marquez")
        @NotBlank(message = "El campo 'author' no debe estar vacío")
        @Size(max = 100, message = "El campo 'author' no debe superar los 100 caracteres")
        String author,

        @Schema(example = "Novela")
        @NotBlank(message = "El campo 'category' no debe estar vacío")
        @Size(max = 100, message = "El campo 'category' no debe superar los 100 caracteres")
        String category,

        @Schema(example = "22.00")
        @NotNull(message = "El campo 'price' no debe estar vacío")
        @DecimalMin(value = "0.0", inclusive = false, message = "El campo 'price' debe ser mayor a 0")
        BigDecimal price,

        @Schema(example = "10")
        @NotNull(message = "El campo 'stock' no debe estar vacío")
        @Min(value = 0, message = "El campo 'stock' no puede ser negativo")
        Integer stock) {
}
