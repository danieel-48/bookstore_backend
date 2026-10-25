package co.edu.sena.auth.controller;

import co.edu.sena.auth.dto.SaleRequest;
import co.edu.sena.auth.dto.SaleResponse;
import co.edu.sena.auth.service.SaleService;
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
 * Endpoints REST de ventas (tabla sales) bajo /api/sales.
 */
@RestController
@RequestMapping("/api/sales")
@Tag(name = "Ventas", description = "Registro y consulta de ventas")
public class SaleController {

    private final SaleService service;

    public SaleController(SaleService service) {
        this.service = service;
    }

    /** POST /api/sales - crea una venta. Responde 201. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar una venta",
            description = "Crea la venta con total 0; el total se acumula al agregar detalles en /api/sale-details.")
    @ApiResponse(responseCode = "201", description = "Venta creada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SaleResponse.class)))
    @ApiResponse(responseCode = "400", description = "Datos invalidos", content = @Content)
    @ApiResponse(responseCode = "404", description = "Cliente o usuario no encontrado", content = @Content)
    public SaleResponse create(@Valid @RequestBody SaleRequest request) {
        return service.create(request);
    }

    /** GET /api/sales - lista todas las ventas (con filtros opcionales de cliente y usuario). */
    @GetMapping
    @Operation(summary = "Consultar ventas", description = "Consulta todas las ventas; admite filtros opcionales por cliente y usuario.")
    @ApiResponse(responseCode = "200", description = "Lista de ventas",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = SaleResponse.class))))
    public List<SaleResponse> list(
            @Parameter(description = "Id del cliente", example = "1") @RequestParam(required = false) Long idClient,
            @Parameter(description = "Id del usuario", example = "1") @RequestParam(required = false) Long idUser) {
        return service.list(idClient, idUser);
    }

    /** GET /api/sales/{id} - detalle de una venta. */
    @GetMapping("/{id}")
    @Operation(summary = "Consultar una venta")
    @ApiResponse(responseCode = "200", description = "Venta encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SaleResponse.class)))
    @ApiResponse(responseCode = "404", description = "Venta no encontrada", content = @Content)
    public SaleResponse get(@PathVariable Long id) {
        return service.get(id);
    }
}
