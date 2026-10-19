package co.edu.sena.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * Resultado de una compra realizada.
 */
@Schema(description = "Resultado de una compra exitosa")
public record PurchaseResponse(
        @Schema(example = "true") boolean success,
        @Schema(example = "Compra realizada") String message,
        @Schema(example = "15") Long saleId,
        @Schema(example = "90000") BigDecimal total) {
}
