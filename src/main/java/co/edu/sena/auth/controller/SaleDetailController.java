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
    @Operation(
            summary = "Agregar detalle de venta",
            description = """
                    ## Agregar detalle de venta
                    Descripción: agrega un libro y cantidad a una venta, descuenta stock y suma al total (HU-16)
                    Método y URL: POST /api/sale-details
                    Body (JSON):
                    {
                      "idSale": 15,
                      "idBook": 7,
                      "quantity": 2
                    }
                    Respuestas:
                    201 Created → detalle agregado a la venta
                    400 Bad Request → datos inválidos o cantidad menor o igual a 0
                    404 Not Found → venta o libro no encontrado
                    409 Conflict → stock insuficiente
                    """
    )
    @ApiResponse(responseCode = "201", description = "Detalle agregado a la venta",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SaleDetailResponse.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos o cantidad menor o igual a 0", content = @Content)
    @ApiResponse(responseCode = "404", description = "Venta o libro no encontrado", content = @Content)
    @ApiResponse(responseCode = "409", description = "Stock insuficiente", content = @Content)
    public SaleDetailResponse create(@Valid @RequestBody SaleDetailRequest request) {
        return service.create(request);
    }

    /** GET /api/sale-details - lista todos los detalles de venta (o filtra por venta). */
    @GetMapping
    @Operation(
            summary = "Consultar detalles de venta",
            description = """
                    ## Consultar detalles de venta
                    Descripción: consulta todos los detalles de venta con filtro opcional por venta (HU-17)
                    Método y URL: GET /api/sale-details
                    Filtro opcional: GET /api/sale-details?idSale=15
                    Body (JSON):
                    No aplica
                    Respuestas:
                    200 OK → lista de detalles de venta
                    """
    )
    @ApiResponse(responseCode = "200", description = "Lista de detalles de venta",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = SaleDetailResponse.class))))
    public List<SaleDetailResponse> list(
            @Parameter(description = "Id de la venta", example = "15") @RequestParam(required = false) Long idSale) {
        return service.list(idSale);
    }

    /** GET /api/sale-details/{id} - consulta un detalle. */
    @GetMapping("/{id}")
    @Operation(
            summary = "Consultar un detalle de venta",
            description = """
                    ## Consultar un detalle de venta
                    Descripción: consulta un detalle de venta por su identificador (HU-18)
                    Método y URL: GET /api/sale-details/{id}
                    Body (JSON):
                    No aplica
                    Respuestas:
                    200 OK → detalle de venta encontrado
                    404 Not Found → detalle no encontrado
                    """
    )
    @ApiResponse(responseCode = "200", description = "Detalle de venta encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SaleDetailResponse.class)))
    @ApiResponse(responseCode = "404", description = "Detalle no encontrado", content = @Content)
    public SaleDetailResponse get(@PathVariable Long id) {
        return service.get(id);
    }
}
