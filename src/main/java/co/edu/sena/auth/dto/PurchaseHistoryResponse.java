package co.edu.sena.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Una compra del historial con los libros que incluyo.
 */
@Schema(description = "Compra del historial de un usuario")
public record PurchaseHistoryResponse(
        @Schema(example = "15") Long saleId,
        LocalDateTime date,
        @Schema(example = "44") BigDecimal total,
        List<PurchaseItemResponse> items) {
}
