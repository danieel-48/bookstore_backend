package co.edu.sena.auth.controller;

import co.edu.sena.auth.dto.PurchaseHistoryResponse;
import co.edu.sena.auth.dto.PurchaseRequest;
import co.edu.sena.auth.dto.PurchaseResponse;
import co.edu.sena.auth.service.PurchaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoints REST de compras bajo /api/purchases.
 */
@RestController
@RequestMapping("/api/purchases")
@Tag(name = "Compras", description = "Pago del carrito e historial de compras")
public class PurchaseController {

    private final PurchaseService service;

    public PurchaseController(PurchaseService service) {
        this.service = service;
    }

    /** POST /api/purchases - compra los libros del carrito. Responde 201. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Comprar libros del carrito",
            description = "Registra la venta, descuenta el stock y vacia el carrito. No hay pasarela de pago: se asume aprobado.")
    @ApiResponse(responseCode = "201", description = "Compra realizada",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PurchaseResponse.class)))
    @ApiResponse(responseCode = "400", description = "Datos invalidos o carrito vacio", content = @Content)
    @ApiResponse(responseCode = "403", description = "Usuario inactivo", content = @Content)
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    @ApiResponse(responseCode = "409", description = "Stock insuficiente", content = @Content)
    public PurchaseResponse buy(@Valid @RequestBody PurchaseRequest request) {
        return service.buy(request.email());
    }

    /** GET /api/purchases - historial de compras del usuario. */
    @GetMapping
    @Operation(summary = "Ver historial de compras", description = "Compras del usuario, de la mas reciente a la mas antigua.")
    @ApiResponse(responseCode = "200", description = "Historial (vacio si no ha comprado)",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = PurchaseHistoryResponse.class))))
    @ApiResponse(responseCode = "400", description = "Email ausente o con formato invalido", content = @Content)
    @ApiResponse(responseCode = "403", description = "Usuario inactivo", content = @Content)
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    public List<PurchaseHistoryResponse> history(
            @Parameter(description = "Email del usuario", example = "ana12345@test.com")
            @RequestParam("email") @NotBlank @Email String email) {
        return service.getHistory(email);
    }
}
