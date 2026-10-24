package co.edu.sena.auth.controller;

import co.edu.sena.auth.dto.BookResponse;
import co.edu.sena.auth.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Endpoints REST del catalogo de libros bajo /api/books.
 */
@RestController
@RequestMapping("/api/books")
@Tag(name = "Libros", description = "Catalogo: busqueda, filtros y detalle")
public class BookController {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
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

    /** GET /api/books/search - busca por titulo, autor o genero. */
    @GetMapping("/search")
    @Operation(summary = "Buscar libros", description = "Busca el texto en titulo, autor o genero.")
    @ApiResponse(responseCode = "200", description = "Libros que coinciden (puede estar vacia)",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = BookResponse.class))))
    @ApiResponse(responseCode = "400", description = "Texto de busqueda vacio", content = @Content)
    public List<BookResponse> search(
            @Parameter(description = "Texto a buscar", example = "soledad")
            @RequestParam("q") @NotBlank String q) {
        return service.search(q);
    }

    /** GET /api/books/{id} - detalle completo de un libro. */
    @GetMapping("/{id}")
    @Operation(summary = "Ver detalle de un libro")
    @ApiResponse(responseCode = "200", description = "Libro encontrado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BookResponse.class)))
    @ApiResponse(responseCode = "404", description = "Libro no encontrado", content = @Content)
    public BookResponse detail(@Parameter(description = "Id del libro", example = "1") @PathVariable("id") Long id) {
        return service.getById(id);
    }
}
