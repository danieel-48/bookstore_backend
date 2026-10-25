package co.edu.sena.auth.service;

import co.edu.sena.auth.dto.BookResponse;
import co.edu.sena.auth.dto.BookRequest;
import co.edu.sena.auth.model.Book;
import co.edu.sena.auth.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

/**
 * Logica de negocio del catalogo de libros: listado con filtros, busqueda y detalle.
 */
@Service
public class BookService {

    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    /**
     * Lista los libros aplicando los filtros opcionales de genero y rango de precio.
     *
     * @throws ResponseStatusException 400 si el precio minimo es mayor que el maximo
     */
    public List<BookResponse> list(String category, BigDecimal minPrice, BigDecimal maxPrice) {
        if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "minPrice no puede ser mayor que maxPrice");
        }
        String cat = category == null || category.isBlank() ? null : category.trim();
        return repository.findAll(cat, minPrice, maxPrice).stream().map(BookResponse::from).toList();
    }

    /** Busca libros por titulo, autor o genero. */
    public List<BookResponse> search(String text) {
        return repository.search(text.trim()).stream().map(BookResponse::from).toList();
    }

    /**
     * Devuelve el detalle completo de un libro.
     *
     * @throws ResponseStatusException 404 si el libro no existe
     */
    public BookResponse getById(Long id) {
        return repository.findById(id)
                .map(BookResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro no encontrado"));
    }

    /** Crea un libro y devuelve el registro con el id asignado por la base de datos. */
    public BookResponse create(BookRequest request) {
        return BookResponse.from(repository.save(toBook(request)));
    }

    /**
     * Reemplaza los datos editables de un libro.
     *
     * @throws ResponseStatusException 404 si el libro no existe
     */
    public BookResponse update(Long id, BookRequest request) {
        if (repository.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro no encontrado");
        }
        Book book = toBook(request);
        book.setId(id);
        repository.update(id, book);
        return getById(id);
    }

    private Book toBook(BookRequest request) {
        Book book = new Book();
        book.setTitle(request.title().trim());
        book.setAuthor(request.author().trim());
        book.setCategory(request.category().trim());
        book.setPrice(request.price());
        book.setStock(request.stock());
        return book;
    }
}
