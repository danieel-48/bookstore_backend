package co.edu.sena.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Datos de entrada para agregar un libro al carrito.
 */
@Schema(description = "Libro y cantidad a agregar al carrito")
public record CartItemRequest(
        @Schema(example = "ana12345@test.com") @NotBlank @Email String email,
        @Schema(example = "1") @NotNull Long bookId,
        @Schema(example = "2") @NotNull @Min(1) Integer quantity) {
}
