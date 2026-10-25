package co.edu.sena.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Una venta (fila de la tabla sales).
 */
@Schema(description = "Venta")
public record SaleResponse(
        @Schema(example = "15") Long idSale,
        @Schema(example = "2026-10-25T01:40:00") LocalDateTime date,
        @Schema(example = "44") BigDecimal total,
        @Schema(example = "1") Long idClient,
        @Schema(example = "1") Long idUser) {
}
