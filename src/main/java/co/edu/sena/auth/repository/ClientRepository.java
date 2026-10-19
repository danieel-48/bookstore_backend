package co.edu.sena.auth.repository;

import co.edu.sena.auth.model.Client;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

/**
 * Acceso a datos de la tabla "clients" mediante JDBC (JdbcTemplate).
 */
@Repository
public class ClientRepository {

    /** Convierte cada fila del ResultSet en un {@link Client}. */
    private static final RowMapper<Client> MAPPER = (rs, i) -> {
        Client c = new Client();
        c.setId(rs.getLong("id_client"));
        c.setName(rs.getString("name"));
        c.setEmail(rs.getString("email"));
        return c;
    };

    private final JdbcTemplate jdbc;

    public ClientRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /** Busca un cliente por email. */
    public Optional<Client> findByEmail(String email) {
        return jdbc.query("SELECT id_client, name, email FROM clients WHERE email = ?", MAPPER, email)
                .stream().findFirst();
    }

    /** Inserta un cliente y devuelve el mismo objeto con su id generado. */
    public Client save(Client client) {
        KeyHolder keys = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO clients (name, email) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, client.getName());
            ps.setString(2, client.getEmail());
            return ps;
        }, keys);
        if (keys.getKey() != null) {
            client.setId(keys.getKey().longValue());
        }
        return client;
    }
}
