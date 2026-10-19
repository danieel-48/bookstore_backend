package co.edu.sena.auth.model;

import java.math.BigDecimal;

/**
 * Entidad que representa una fila de la tabla "sale_details" (un libro dentro de una compra).
 */
public class SaleDetail {

    /** Columna id_detail. */
    private Long id;
    /** Columna id_sale. */
    private Long saleId;
    /** Columna id_book. */
    private Long bookId;
    /** Titulo del libro; no es columna de sale_details, se obtiene al consultar el historial. */
    private String bookTitle;
    /** Columna quantity. */
    private int quantity;
    /** Columna total (precio por cantidad). */
    private BigDecimal total;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSaleId() { return saleId; }
    public void setSaleId(Long saleId) { this.saleId = saleId; }

    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }

    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
}
