package co.edu.sena.auth.dto;

import co.edu.sena.auth.model.Book;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * Informacion de un libro del catalogo.
 */
@Schema(description = "Libro del catalogo")
public record BookResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "Cien anos de soledad") String title,
        @Schema(example = "Gabriel Garcia Marquez") String author,
        @Schema(example = "Novela") String category,
        @Schema(example = "45000") BigDecimal price,
        @Schema(example = "10") int stock) {

    /** Convierte la entidad {@link Book} en su representacion publica. */
    public static BookResponse from(Book b) {
        return new BookResponse(b.getId(), b.getTitle(), b.getAuthor(), b.getCategory(), b.getPrice(), b.getStock());
    }
}
