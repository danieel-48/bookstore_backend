package co.edu.sena.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Datos para agregar un detalle a una venta (tabla sale_details). El total se calcula con el precio del libro.
 */
@Schema(description = "Datos para crear un detalle de venta")
public record SaleDetailRequest(
        @Schema(example = "15") @NotNull Long idSale,
        @Schema(example = "7") @NotNull Long idBook,
        @Schema(example = "2") @NotNull @Min(1) Integer quantity) {
}
