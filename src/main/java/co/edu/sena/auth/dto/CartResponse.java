package co.edu.sena.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

/**
 * Contenido del carrito de un usuario y su total.
 */
@Schema(description = "Carrito de compras de un usuario")
public record CartResponse(
        List<CartItemResponse> items,
        @Schema(example = "44") BigDecimal total) {
}
