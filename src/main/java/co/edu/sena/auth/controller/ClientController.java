package co.edu.sena.auth.controller;

import co.edu.sena.auth.dto.ClientRequest;
import co.edu.sena.auth.dto.ClientResponse;
import co.edu.sena.auth.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
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
 * Endpoints REST de clientes (tabla clients) bajo /api/clients.
 */
@RestController
@RequestMapping("/api/clients")
@Tag(name = "Clientes", description = "Registro, listado y actualizacion de clientes")
public class ClientController {

    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    /** POST /api/clients - crea un cliente. Responde 201. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar un cliente", description = "Solo name es obligatorio. El email y el documento no pueden repetirse.")
    @ApiResponse(responseCode = "201", description = "Cliente creado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientResponse.class)))
    @ApiResponse(responseCode = "400", description = "Datos invalidos", content = @Content)
    @ApiResponse(responseCode = "409", description = "Email o documento ya registrados", content = @Content)
    public ClientResponse create(@Valid @RequestBody ClientRequest request) {
        return service.create(request);
    }

    /** GET /api/clients - lista los clientes. */
    @GetMapping
    @Operation(summary = "Listar clientes")
    @ApiResponse(responseCode = "200", description = "Lista de clientes",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ClientResponse.class))))
    public List<ClientResponse> list() {
        return service.list();
    }

    /** PUT /api/clients/{id} - actualiza un cliente. */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un cliente", description = "Reemplaza todos los datos del cliente.")
    @ApiResponse(responseCode = "200", description = "Cliente actualizado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientResponse.class)))
    @ApiResponse(responseCode = "400", description = "Datos invalidos", content = @Content)
    @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content)
    @ApiResponse(responseCode = "409", description = "Email o documento ya registrados por otro cliente", content = @Content)
    public ClientResponse update(@PathVariable Long id, @Valid @RequestBody ClientRequest request) {
        return service.update(id, request);
    }
}
