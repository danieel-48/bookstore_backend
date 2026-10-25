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
    @Operation(
            summary = "Registrar cliente",
            description = """
                    ## Registrar cliente
                    Descripción: registra un cliente; solo el nombre es obligatorio (HU-10)
                    Método y URL: POST /api/clients
                    Body (JSON):
                    {
                      "idNumber": 1012345678,
                      "name": "Ana Ariza",
                      "email": "ana12345@test.com",
                      "phone": 3001234567
                    }
                    Respuestas:
                    201 Created → cliente creado exitosamente
                    400 Bad Request → datos inválidos o nombre vacío
                    409 Conflict → documento o email ya registrados
                    """
    )
    @ApiResponse(responseCode = "201", description = "Cliente creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientResponse.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos o nombre vacío", content = @Content)
    @ApiResponse(responseCode = "409", description = "Documento o email ya registrados", content = @Content)
    public ClientResponse create(@Valid @RequestBody ClientRequest request) {
        return service.create(request);
    }

    /** GET /api/clients - lista los clientes. */
    @GetMapping
    @Operation(
            summary = "Listar clientes",
            description = """
                    ## Listar clientes
                    Descripción: lista todos los clientes registrados (HU-11)
                    Método y URL: GET /api/clients
                    Body (JSON):
                    No aplica
                    Respuestas:
                    200 OK → lista de clientes registrados
                    """
    )
    @ApiResponse(responseCode = "200", description = "Lista de clientes registrados",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ClientResponse.class))))
    public List<ClientResponse> list() {
        return service.list();
    }

    /** PUT /api/clients/{id} - actualiza un cliente. */
    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar cliente",
            description = """
                    ## Actualizar cliente
                    Descripción: reemplaza todos los datos del cliente indicado (HU-12)
                    Método y URL: PUT /api/clients/{id}
                    Body (JSON):
                    {
                      "idNumber": 1012345678,
                      "name": "Ana Ariza",
                      "email": "ana12345@test.com",
                      "phone": 3009876543
                    }
                    Respuestas:
                    200 OK → cliente actualizado exitosamente
                    400 Bad Request → datos inválidos
                    404 Not Found → cliente no encontrado
                    409 Conflict → documento o email ya registrados por otro cliente
                    """
    )
    @ApiResponse(responseCode = "200", description = "Cliente actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientResponse.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content)
    @ApiResponse(responseCode = "409", description = "Documento o email ya registrados por otro cliente", content = @Content)
    public ClientResponse update(@PathVariable Long id, @Valid @RequestBody ClientRequest request) {
        return service.update(id, request);
    }
}
