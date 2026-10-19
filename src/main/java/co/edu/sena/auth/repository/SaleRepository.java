package co.edu.sena.auth.repository;

import co.edu.sena.auth.model.Sale;
import co.edu.sena.auth.model.SaleDetail;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Acceso a datos de las tablas "sales" y "sale_details" mediante JDBC (JdbcTemplate).
 */
@Repository
public class SaleRepository {

    /** Convierte cada fila de "sales" en un {@link Sale}. */
    private static final RowMapper<Sale> SALE_MAPPER = (rs, i) -> {
        Sale s = new Sale();
        s.setId(rs.getLong("id_sale"));
        Timestamp date = rs.getTimestamp("date");
        s.setDate(date == null ? null : date.toLocalDateTime());
        s.setTotal(rs.getBigDecimal("total"));
        s.setClientId(rs.getLong("id_client"));
        s.setUserId(rs.getLong("id_user"));
        return s;
    };

    /** Convierte cada fila de "sale_details" (con el titulo del libro) en un {@link SaleDetail}. */
    private static final RowMapper<SaleDetail> DETAIL_MAPPER = (rs, i) -> {
        SaleDetail d = new SaleDetail();
        d.setId(rs.getLong("id_detail"));
        d.setSaleId(rs.getLong("id_sale"));
        d.setBookId(rs.getLong("id_book"));
        d.setBookTitle(rs.getString("title"));
        d.setQuantity(rs.getInt("quantity"));
        d.setTotal(rs.getBigDecimal("total"));
        return d;
    };

    private final JdbcTemplate jdbc;

    public SaleRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /** Crea una compra con total 0 y la fecha actual; devuelve su id. */
    public long createSale(Long clientId, Long userId) {
        KeyHolder keys = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO sales (date, total, id_client, id_user) VALUES (?, 0, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setLong(2, clientId);
            ps.setLong(3, userId);
            return ps;
        }, keys);
        return keys.getKey().longValue();
    }

    /** Agrega un libro a una compra. */
    public void addDetail(long saleId, Long bookId, int quantity, BigDecimal total) {
        jdbc.update("INSERT INTO sale_details (id_sale, id_book, quantity, total) VALUES (?, ?, ?, ?)",
                saleId, bookId, quantity, total);
    }

    /** Actualiza el total de una compra. */
    public void updateTotal(long saleId, BigDecimal total) {
        jdbc.update("UPDATE sales SET total = ? WHERE id_sale = ?", total, saleId);
    }

    /** Lista las compras de un usuario, de la mas reciente a la mas antigua. */
    public List<Sale> findByUserId(Long userId) {
        return jdbc.query("SELECT id_sale, date, total, id_client, id_user FROM sales "
                + "WHERE id_user = ? ORDER BY date DESC, id_sale DESC", SALE_MAPPER, userId);
    }

    /** Lista los detalles de todas las compras de un usuario, con el titulo de cada libro. */
    public List<SaleDetail> findDetailsByUserId(Long userId) {
        return jdbc.query("SELECT d.id_detail, d.id_sale, d.id_book, b.title, d.quantity, d.total "
                + "FROM sale_details d "
                + "JOIN sales s ON s.id_sale = d.id_sale "
                + "JOIN books b ON b.id_book = d.id_book "
                + "WHERE s.id_user = ? ORDER BY d.id_detail", DETAIL_MAPPER, userId);
    }
}
