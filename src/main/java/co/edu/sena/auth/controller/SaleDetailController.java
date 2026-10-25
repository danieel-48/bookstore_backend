package co.edu.sena.auth.controller;

import co.edu.sena.auth.dto.SaleDetailRequest;
import co.edu.sena.auth.dto.SaleDetailResponse;
import co.edu.sena.auth.service.SaleDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoints REST de detalles de venta (tabla sale_details) bajo /api/sale-details.
 */
@RestController
@RequestMapping("/api/sale-details")
@Tag(name = "Detalles de venta", description = "Libros incluidos en cada venta")
public class SaleDetailController {

    private final SaleDetailService service;

    public SaleDetailController(SaleDetailService service) {
        this.service = service;
    }

    /** POST /api/sale-details - agrega un libro a una venta. Responde 201. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Agregar un detalle a una venta",
            description = "Calcula el total con el precio del libro, descuenta el stock y suma el total a la venta.")
    @ApiResponse(responseCode = "201", description = "Detalle creado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SaleDetailResponse.class)))
    @ApiResponse(responseCode = "400", description = "Datos invalidos", content = @Content)
    @ApiResponse(responseCode = "404", description = "Venta o libro no encontrado", content = @Content)
    @ApiResponse(responseCode = "409", description = "Stock insuficiente", content = @Content)
    public SaleDetailResponse create(@Valid @RequestBody SaleDetailRequest request) {
        return service.create(request);
    }

    /** GET /api/sale-details - lista detalles, opcionalmente de una venta. */
    @GetMapping
    @Operation(summary = "Listar detalles de venta", description = "Todos los detalles; filtro opcional por venta.")
    @ApiResponse(responseCode = "200", description = "Lista de detalles",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = SaleDetailResponse.class))))
    public List<SaleDetailResponse> list(
            @Parameter(description = "Id de la venta", example = "15") @RequestParam(required = false) Long idSale) {
        return service.list(idSale);
    }

    /** GET /api/sale-details/{id} - consulta un detalle. */
    @GetMapping("/{id}")
    @Operation(summary = "Consultar un detalle de venta")
    @ApiResponse(responseCode = "200", description = "Detalle encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SaleDetailResponse.class)))
    @ApiResponse(responseCode = "404", description = "Detalle no encontrado", content = @Content)
    public SaleDetailResponse get(@PathVariable Long id) {
        return service.get(id);
    }
}
