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
    @Operation(
            summary = "Registrar venta",
            description = """
                    ## Registrar venta
                    Descripción: registra una venta con todos los campos obligatorios: date, idClient e idUser (HU-14)
                    Método y URL: POST /api/sales
                    Body (JSON):
                    {
                      "date": "2026-10-25T01:40:00",
                      "idClient": 2,
                      "idUser": 5
                    }
                    Respuestas:
                    201 Created → venta creada exitosamente
                    400 Bad Request → campos vacíos o datos inválidos
                    404 Not Found → cliente o usuario no encontrado
                    """
    )
    @ApiResponse(responseCode = "201", description = "Venta creada exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SaleResponse.class)))
    @ApiResponse(responseCode = "400", description = "Campos vacíos o datos inválidos", content = @Content)
    @ApiResponse(responseCode = "404", description = "Cliente o usuario no encontrado", content = @Content)
    public SaleResponse create(@Valid @RequestBody SaleRequest request) {
        return service.create(request);
    }

    /** GET /api/sales - lista todas las ventas (con filtros opcionales de cliente y usuario). */
    @GetMapping
    @Operation(
            summary = "Consultar todas las ventas",
            description = """
                    ## Consultar todas las ventas
                    Descripción: consulta todas las ventas registradas con filtros opcionales por cliente y usuario (HU-13)
                    Método y URL: GET /api/sales
                    Filtros opcionales: GET /api/sales?idClient=2&idUser=5
                    Body (JSON):
                    No aplica
                    Respuestas:
                    200 OK → lista de ventas registradas
                    """
    )
    @ApiResponse(responseCode = "200", description = "Lista de ventas registradas",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = SaleResponse.class))))
    public List<SaleResponse> list(
            @Parameter(description = "Id del cliente", example = "1") @RequestParam(required = false) Long idClient,
            @Parameter(description = "Id del usuario", example = "1") @RequestParam(required = false) Long idUser) {
        return service.list(idClient, idUser);
    }

    /** GET /api/sales/{id} - detalle de una venta. */
    @GetMapping("/{id}")
    @Operation(
            summary = "Consultar una venta",
            description = """
                    ## Consultar una venta
                    Descripción: consulta los datos y el total de una venta por su identificador (HU-15)
                    Método y URL: GET /api/sales/{id}
                    Body (JSON):
                    No aplica
                    Respuestas:
                    200 OK → venta encontrada
                    404 Not Found → venta no encontrada
                    """
    )
    @ApiResponse(responseCode = "200", description = "Venta encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SaleResponse.class)))
    @ApiResponse(responseCode = "404", description = "Venta no encontrada", content = @Content)
    public SaleResponse get(@PathVariable Long id) {
        return service.get(id);
    }
}
