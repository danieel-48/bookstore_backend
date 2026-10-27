package co.edu.sena.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Datos para agregar un detalle a una venta (tabla sale_details). Todos los campos son obligatorios.
 */
@Schema(description = "Datos para crear un detalle de venta")
public record SaleDetailRequest(
        @Schema(description = "Id de la venta (obligatorio)", example = "15")
        @NotNull(message = "El campo 'idSale' no debe estar vacío")
        @Positive(message = "El campo 'idSale' debe ser un número positivo")
        Long idSale,

        @Schema(description = "Id del libro (obligatorio)", example = "7")
        @NotNull(message = "El campo 'idBook' no debe estar vacío")
        @Positive(message = "El campo 'idBook' debe ser un número positivo")
        Long idBook,

        @Schema(description = "Cantidad vendida (obligatoria, mayor a 0)", example = "2")
        @NotNull(message = "El campo 'quantity' no debe estar vacío")
        @Min(value = 1, message = "El campo 'quantity' debe ser mayor a 0")
        Integer quantity) {
}
