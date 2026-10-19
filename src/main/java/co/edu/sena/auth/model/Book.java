package co.edu.sena.auth.model;

import java.math.BigDecimal;

/**
 * Entidad que representa una fila de la tabla "books".
 */
public class Book {

    /** Columna id_book. */
    private Long id;
    /** Columna title. */
    private String title;
    /** Columna author (puede ser nula). */
    private String author;
    /** Columna category: genero del libro (puede ser nula). */
    private String category;
    /** Columna price. */
    private BigDecimal price;
    /** Columna stock: unidades disponibles. */
    private int stock;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
}
