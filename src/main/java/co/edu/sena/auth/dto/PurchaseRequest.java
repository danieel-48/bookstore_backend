package co.edu.sena.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Datos de entrada para comprar los libros del carrito.
 */
@Schema(description = "Usuario que paga el contenido de su carrito")
public record PurchaseRequest(
        @Schema(example = "ana12345@test.com") @NotBlank @Email String email) {
}
