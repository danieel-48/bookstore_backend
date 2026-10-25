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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Acceso a datos de las tablas "sales" y "sale_details" mediante JDBC (JdbcTemplate).
 */
@Repository
public class SaleRepository {

    private static final String SALE_COLUMNS = "id_sale, date, total, id_client, id_user";
    private static final String DETAIL_COLUMNS = "id_detail, id_sale, id_book, quantity, total";

    /** Convierte cada fila de "sales" en un {@link Sale}. */
    private static final RowMapper<Sale> SALE_MAPPER = (rs, i) -> {
        Sale s = new Sale();
        s.setId(rs.getLong("id_sale"));
        Timestamp date = rs.getTimestamp("date");
        s.setDate(date == null ? null : date.toLocalDateTime());
        s.setTotal(rs.getBigDecimal("total"));
        s.setClientId(rs.getLong("id_client"));
        s.setUserId(rs.getObject("id_user", Long.class));
        return s;
    };

    /** Convierte cada fila de "sale_details" en un {@link SaleDetail}. */
    private static final RowMapper<SaleDetail> DETAIL_MAPPER = (rs, i) -> {
        SaleDetail d = new SaleDetail();
        d.setId(rs.getLong("id_detail"));
        d.setSaleId(rs.getLong("id_sale"));
        d.setBookId(rs.getLong("id_book"));
        d.setQuantity(rs.getInt("quantity"));
        d.setTotal(rs.getBigDecimal("total"));
        return d;
    };

    private final JdbcTemplate jdbc;

    public SaleRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /** Crea una venta con total 0; devuelve su id. */
    public long createSale(LocalDateTime date, Long clientId, Long userId) {
        KeyHolder keys = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO sales (date, total, id_client, id_user) VALUES (?, 0, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setTimestamp(1, Timestamp.valueOf(date));
            ps.setLong(2, clientId);
            ps.setObject(3, userId);
            return ps;
        }, keys);
        return keys.getKey().longValue();
    }

    /** Busca una venta por id. */
    public Optional<Sale> findById(long saleId) {
        return jdbc.query("SELECT " + SALE_COLUMNS + " FROM sales WHERE id_sale = ?", SALE_MAPPER, saleId)
                .stream().findFirst();
    }

    /** Lista ventas, de la mas reciente a la mas antigua; los filtros nulos se ignoran. */
    public List<Sale> findAll(Long clientId, Long userId) {
        StringBuilder sql = new StringBuilder("SELECT " + SALE_COLUMNS + " FROM sales WHERE 1=1");
        List<Object> args = new ArrayList<>();
        if (clientId != null) {
            sql.append(" AND id_client = ?");
            args.add(clientId);
        }
        if (userId != null) {
            sql.append(" AND id_user = ?");
            args.add(userId);
        }
        sql.append(" ORDER BY date DESC, id_sale DESC");
        return jdbc.query(sql.toString(), SALE_MAPPER, args.toArray());
    }

    /** Suma un monto al total de una venta. */
    public void addToTotal(long saleId, BigDecimal amount) {
        jdbc.update("UPDATE sales SET total = total + ? WHERE id_sale = ?", amount, saleId);
    }

    /** Inserta un detalle y devuelve su id. */
    public long addDetail(long saleId, Long bookId, int quantity, BigDecimal total) {
        KeyHolder keys = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO sale_details (id_sale, id_book, quantity, total) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, saleId);
            ps.setLong(2, bookId);
            ps.setInt(3, quantity);
            ps.setBigDecimal(4, total);
            return ps;
        }, keys);
        return keys.getKey().longValue();
    }

    /** Busca un detalle por id. */
    public Optional<SaleDetail> findDetailById(long detailId) {
        return jdbc.query("SELECT " + DETAIL_COLUMNS + " FROM sale_details WHERE id_detail = ?",
                DETAIL_MAPPER, detailId).stream().findFirst();
    }

    /** Lista detalles; si saleId es nulo devuelve todos. */
    public List<SaleDetail> findDetails(Long saleId) {
        if (saleId == null) {
            return jdbc.query("SELECT " + DETAIL_COLUMNS + " FROM sale_details ORDER BY id_detail", DETAIL_MAPPER);
        }
        return jdbc.query("SELECT " + DETAIL_COLUMNS + " FROM sale_details WHERE id_sale = ? ORDER BY id_detail",
                DETAIL_MAPPER, saleId);
    }
}
