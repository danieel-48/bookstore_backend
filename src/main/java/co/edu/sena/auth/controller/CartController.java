package co.edu.sena.auth.controller;

import co.edu.sena.auth.dto.CartItemRequest;
import co.edu.sena.auth.dto.CartResponse;
import co.edu.sena.auth.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints REST del carrito de compras bajo /api/cart.
 */
@RestController
@RequestMapping("/api/cart")
@Tag(name = "Carrito", description = "Carrito de compras del usuario (se guarda en memoria)")
public class CartController {

    private final CartService service;

    public CartController(CartService service) {
        this.service = service;
    }

    /** POST /api/cart/items - agrega un libro al carrito. */
    @PostMapping("/items")
    @Operation(summary = "Agregar libro al carrito",
            description = "Suma la cantidad si el libro ya estaba en el carrito y devuelve el carrito actualizado.")
    @ApiResponse(responseCode = "200", description = "Libro agregado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CartResponse.class)))
    @ApiResponse(responseCode = "400", description = "Datos invalidos", content = @Content)
    @ApiResponse(responseCode = "403", description = "Usuario inactivo", content = @Content)
    @ApiResponse(responseCode = "404", description = "Usuario o libro no encontrado", content = @Content)
    @ApiResponse(responseCode = "409", description = "La cantidad supera el stock disponible", content = @Content)
    public CartResponse addItem(@Valid @RequestBody CartItemRequest request) {
        return service.addItem(request);
    }

    /** GET /api/cart - consulta el carrito del usuario. */
    @GetMapping
    @Operation(summary = "Ver carrito")
    @ApiResponse(responseCode = "200", description = "Carrito del usuario (items vacios si no tiene libros)",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CartResponse.class)))
    @ApiResponse(responseCode = "400", description = "Email ausente o con formato invalido", content = @Content)
    @ApiResponse(responseCode = "403", description = "Usuario inactivo", content = @Content)
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    public CartResponse getCart(
            @Parameter(description = "Email del usuario", example = "ana12345@test.com")
            @RequestParam("email") @NotBlank @Email String email) {
        return service.getCart(email);
    }
}
