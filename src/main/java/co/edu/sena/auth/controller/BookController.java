package co.edu.sena.auth.controller;

import co.edu.sena.auth.dto.BookResponse;
import co.edu.sena.auth.dto.BookRequest;
import co.edu.sena.auth.service.BookService;
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

import java.math.BigDecimal;
import java.util.List;

/**
 * Endpoints REST del catalogo de libros bajo /api/books.
 */
@RestController
@RequestMapping("/api/books")
@Tag(name = "Libros", description = "Catalogo: listado y filtros")
public class BookController {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    /** POST /api/books - crea un libro en el catalogo. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Registrar libro",
            description = """
                    ## Registrar libro
                    Descripción: registra un nuevo libro en el catálogo con todos los campos obligatorios (HU-06)
                    Método y URL: POST /api/books
                    Body (JSON):
                    {
                      "title": "Harry Potter",
                      "author": "J.K Rowling",
                      "category": "Fantasia",
                      "price": 22.00,
                      "stock": 20
                    }
                    Respuestas:
                    201 Created → libro creado exitosamente
                    400 Bad Request → campos vacíos o valores numéricos inválidos
                    """
    )
    @ApiResponse(responseCode = "201", description = "Libro creado exitosamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BookResponse.class)))
    @ApiResponse(responseCode = "400", description = "Campos vacíos o valores numéricos inválidos", content = @Content)
    public BookResponse create(@Valid @RequestBody BookRequest request) {
        return service.create(request);
    }

    /** PUT /api/books/{id} - reemplaza los datos editables de un libro. */
    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar libro",
            description = """
                    ## Actualizar libro
                    Descripción: reemplaza la información del libro indicado (HU-07)
                    Método y URL: PUT /api/books/{id}
                    Body (JSON):
                    {
                      "title": "Harry Potter",
                      "author": "J.K Rowling",
                      "category": "Fantasia",
                      "price": 25.00,
                      "stock": 18
                    }
                    Respuestas:
                    200 OK → libro actualizado exitosamente
                    400 Bad Request → datos inválidos
                    404 Not Found → libro no encontrado
                    """
    )
    @ApiResponse(responseCode = "200", description = "Libro actualizado exitosamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BookResponse.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    @ApiResponse(responseCode = "404", description = "Libro no encontrado", content = @Content)
    public BookResponse update(
            @Parameter(description = "Id del libro", example = "1") @PathVariable("id") Long id,
            @Valid @RequestBody BookRequest request) {
        return service.update(id, request);
    }

    /** GET /api/books - lista libros con filtros opcionales de genero y precio. */
    @GetMapping
    @Operation(
            summary = "Consultar y filtrar libros",
            description = """
                    ## Consulta todos los libros y filtros
                    Descripción: consulta el catálogo completo de libros o filtra por género y precio (HU-08 / HU-09)
                    Método y URL: GET /api/books
                    Filtros opcionales: GET /api/books?category=Fantasia&minPrice=10&maxPrice=50
                    Body (JSON):
                    No aplica
                    Respuestas:
                    200 OK → lista de libros (catálogo completo o filtrado)
                    400 Bad Request → precio mínimo mayor que el máximo
                    """
    )
    @ApiResponse(responseCode = "200", description = "Lista con los libros del catálogo",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = BookResponse.class))))
    @ApiResponse(responseCode = "400", description = "Precio mínimo mayor que el máximo", content = @Content)
    public List<BookResponse> list(
            @Parameter(description = "Genero exacto", example = "Fantasia")
            @RequestParam(name = "category", required = false) String category,
            @Parameter(description = "Precio minimo (inclusive)", example = "10")
            @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
            @Parameter(description = "Precio maximo (inclusive)", example = "50")
            @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice) {
        return service.list(category, minPrice, maxPrice);
    }
}
