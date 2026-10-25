package co.edu.sena.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * Un detalle de venta (fila de la tabla sale_details).
 */
@Schema(description = "Detalle de venta")
public record SaleDetailResponse(
        @Schema(example = "31") Long idDetail,
        @Schema(example = "15") Long idSale,
        @Schema(example = "7") Long idBook,
        @Schema(example = "2") int quantity,
        @Schema(example = "44") BigDecimal total) {
}
