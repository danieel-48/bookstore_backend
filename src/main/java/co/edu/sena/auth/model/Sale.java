package co.edu.sena.auth.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad que representa una fila de la tabla "sales" (una compra).
 */
public class Sale {

    /** Columna id_sale. */
    private Long id;
    /** Columna date (puede ser nula). */
    private LocalDateTime date;
    /** Columna total. */
    private BigDecimal total;
    /** Columna id_client. */
    private Long clientId;
    /** Columna id_user (puede ser nula). */
    private Long userId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
