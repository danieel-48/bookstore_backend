package co.edu.sena.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * Un libro dentro del carrito con su subtotal.
 */
@Schema(description = "Libro dentro del carrito")
public record CartItemResponse(
        @Schema(example = "1") Long bookId,
        @Schema(example = "Cien anos de soledad") String title,
        @Schema(example = "2") int quantity,
        @Schema(example = "22") BigDecimal unitPrice,
        @Schema(example = "44") BigDecimal subtotal) {
}
