package co.edu.sena.auth.repository;

import co.edu.sena.auth.model.Book;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Acceso a datos de la tabla "books" mediante JDBC (JdbcTemplate).
 */
@Repository
public class BookRepository {

    private static final String SELECT = "SELECT id_book, title, author, category, price, stock FROM books";

    /** Convierte cada fila del ResultSet en un {@link Book}. */
    private static final RowMapper<Book> MAPPER = (rs, i) -> {
        Book b = new Book();
        b.setId(rs.getLong("id_book"));
        b.setTitle(rs.getString("title"));
        b.setAuthor(rs.getString("author"));
        b.setCategory(rs.getString("category"));
        b.setPrice(rs.getBigDecimal("price"));
        b.setStock(rs.getInt("stock"));
        return b;
    };

    private final JdbcTemplate jdbc;

    public BookRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Lista los libros aplicando solo los filtros que no sean nulos.
     *
     * @param category genero exacto (sin distinguir mayusculas)
     * @param minPrice precio minimo, inclusive
     * @param maxPrice precio maximo, inclusive
     */
    public List<Book> findAll(String category, BigDecimal minPrice, BigDecimal maxPrice) {
        StringBuilder sql = new StringBuilder(SELECT).append(" WHERE 1 = 1");
        List<Object> args = new ArrayList<>();
        if (category != null) {
            sql.append(" AND LOWER(category) = LOWER(?)");
            args.add(category);
        }
        if (minPrice != null) {
            sql.append(" AND price >= ?");
            args.add(minPrice);
        }
        if (maxPrice != null) {
            sql.append(" AND price <= ?");
            args.add(maxPrice);
        }
        sql.append(" ORDER BY title");
        return jdbc.query(sql.toString(), MAPPER, args.toArray());
    }

    /** Busca libros cuyo titulo, autor o genero contenga el texto indicado. */
    public List<Book> search(String text) {
        String like = "%" + text + "%";
        return jdbc.query(SELECT + " WHERE title LIKE ? OR author LIKE ? OR category LIKE ? ORDER BY title",
                MAPPER, like, like, like);
    }

    /** Busca un libro por id. */
    public Optional<Book> findById(Long id) {
        return jdbc.query(SELECT + " WHERE id_book = ?", MAPPER, id).stream().findFirst();
    }

    /** Inserta un libro y devuelve la entidad con su id generado. */
    public Book save(Book book) {
        KeyHolder keys = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO books (title, author, category, price, stock) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getCategory());
            statement.setBigDecimal(4, book.getPrice());
            statement.setInt(5, book.getStock());
            return statement;
        }, keys);
        if (keys.getKey() == null) {
            throw new IllegalStateException("La base de datos no devolvio el id del libro creado");
        }
        book.setId(keys.getKey().longValue());
        return book;
    }

    /** Actualiza todos los campos editables de un libro. */
    public int update(Long id, Book book) {
        return jdbc.update("UPDATE books SET title = ?, author = ?, category = ?, price = ?, stock = ? "
                        + "WHERE id_book = ?",
                book.getTitle(), book.getAuthor(), book.getCategory(), book.getPrice(), book.getStock(), id);
    }

    /**
     * Descuenta stock de forma atomica: solo actualiza si hay unidades suficientes.
     *
     * @return true si se descontaron las unidades, false si el stock no alcanzaba
     */
    public boolean decreaseStock(Long id, int quantity) {
        return jdbc.update("UPDATE books SET stock = stock - ? WHERE id_book = ? AND stock >= ?",
                quantity, id, quantity) == 1;
    }
}
