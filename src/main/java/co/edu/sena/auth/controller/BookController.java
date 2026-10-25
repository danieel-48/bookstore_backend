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
    @Operation(summary = "Registrar libro", description = "Crea un libro. Todos los campos son obligatorios: title, author, category, price y stock.")
    @ApiResponse(responseCode = "201", description = "Libro creado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BookResponse.class)))
    @ApiResponse(responseCode = "400", description = "Datos invalidos", content = @Content)
    public BookResponse create(@Valid @RequestBody BookRequest request) {
        return service.create(request);
    }

    /** PUT /api/books/{id} - reemplaza los datos editables de un libro. */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar libro", description = "Reemplaza los datos del libro indicado. Todos los campos son obligatorios.")
    @ApiResponse(responseCode = "200", description = "Libro actualizado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BookResponse.class)))
    @ApiResponse(responseCode = "400", description = "Datos invalidos", content = @Content)
    @ApiResponse(responseCode = "404", description = "Libro no encontrado", content = @Content)
    public BookResponse update(
            @Parameter(description = "Id del libro", example = "1") @PathVariable("id") Long id,
            @Valid @RequestBody BookRequest request) {
        return service.update(id, request);
    }

    /** GET /api/books - lista libros con filtros opcionales de genero y precio. */
    @GetMapping
    @Operation(summary = "Listar y filtrar libros",
            description = "Sin parametros devuelve todo el catalogo. Los filtros son opcionales y se pueden combinar. "
                    + "El filtro por idioma no esta disponible porque la tabla books no tiene esa columna.")
    @ApiResponse(responseCode = "200", description = "Lista de libros (puede estar vacia)",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = BookResponse.class))))
    @ApiResponse(responseCode = "400", description = "minPrice mayor que maxPrice", content = @Content)
    public List<BookResponse> list(
            @Parameter(description = "Genero exacto", example = "Novela")
            @RequestParam(name = "category", required = false) String category,
            @Parameter(description = "Precio minimo (inclusive)", example = "10000")
            @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
            @Parameter(description = "Precio maximo (inclusive)", example = "50000")
            @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice) {
        return service.list(category, minPrice, maxPrice);
    }
}
