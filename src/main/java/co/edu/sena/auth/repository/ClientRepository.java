package co.edu.sena.auth.repository;

import co.edu.sena.auth.model.Client;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

/**
 * Acceso a datos de la tabla "clients" mediante JDBC (JdbcTemplate).
 */
@Repository
public class ClientRepository {

    private static final String COLUMNS = "id_client, id_number, name, email, phone";

    /** Convierte cada fila del ResultSet en un {@link Client}. */
    private static final RowMapper<Client> MAPPER = (rs, i) -> {
        Client c = new Client();
        c.setId(rs.getLong("id_client"));
        c.setIdNumber(rs.getObject("id_number", Long.class));
        c.setName(rs.getString("name"));
        c.setEmail(rs.getString("email"));
        c.setPhone(rs.getObject("phone", Long.class));
        return c;
    };

    private final JdbcTemplate jdbc;

    public ClientRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /** Lista todos los clientes. */
    public List<Client> findAll() {
        return jdbc.query("SELECT " + COLUMNS + " FROM clients ORDER BY id_client", MAPPER);
    }

    /** Busca un cliente por id. */
    public Optional<Client> findById(Long id) {
        return jdbc.query("SELECT " + COLUMNS + " FROM clients WHERE id_client = ?", MAPPER, id)
                .stream().findFirst();
    }

    /** Indica si existe un cliente con ese id. */
    public boolean existsById(Long id) {
        Integer n = jdbc.queryForObject("SELECT COUNT(*) FROM clients WHERE id_client = ?", Integer.class, id);
        return n != null && n > 0;
    }

    /** Indica si otro cliente (distinto de excludeId; use -1 al crear) ya tiene ese email. */
    public boolean existsByEmailAndIdNot(String email, Long excludeId) {
        Integer n = jdbc.queryForObject("SELECT COUNT(*) FROM clients WHERE email = ? AND id_client <> ?",
                Integer.class, email, excludeId);
        return n != null && n > 0;
    }

    /** Indica si otro cliente (distinto de excludeId; use -1 al crear) ya tiene ese documento. */
    public boolean existsByIdNumberAndIdNot(Long idNumber, Long excludeId) {
        Integer n = jdbc.queryForObject("SELECT COUNT(*) FROM clients WHERE id_number = ? AND id_client <> ?",
                Integer.class, idNumber, excludeId);
        return n != null && n > 0;
    }

    /** Inserta un cliente y devuelve el mismo objeto con su id generado. */
    public Client save(Client client) {
        KeyHolder keys = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO clients (id_number, name, email, phone) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, client.getIdNumber(), Types.BIGINT);
            ps.setString(2, client.getName());
            ps.setString(3, client.getEmail());
            ps.setObject(4, client.getPhone(), Types.BIGINT);
            return ps;
        }, keys);
        client.setId(keys.getKey().longValue());
        return client;
    }

    /** Actualiza todos los campos de un cliente; devuelve las filas afectadas. */
    public int update(Long id, Client client) {
        return jdbc.update("UPDATE clients SET id_number = ?, name = ?, email = ?, phone = ? WHERE id_client = ?",
                client.getIdNumber(), client.getName(), client.getEmail(), client.getPhone(), id);
    }
}
