package co.edu.sena.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * Un libro comprado dentro de una compra del historial.
 */
@Schema(description = "Libro comprado dentro de una compra")
public record PurchaseItemResponse(
        @Schema(example = "1") Long bookId,
        @Schema(example = "Cien anos de soledad") String title,
        @Schema(example = "2") int quantity,
        @Schema(example = "44") BigDecimal total) {
}
