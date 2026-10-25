package co.edu.sena.auth.repository;

import co.edu.sena.auth.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

/**
 * Acceso a datos de la tabla "users" mediante JDBC (JdbcTemplate).
 */
@Repository
public class UserRepository {

    private static final String COLUMNS = "id_user, name, email, password, role, is_active";

    /** Convierte cada fila del ResultSet en un {@link User}. */
    private static final RowMapper<User> MAPPER = (rs, i) -> {
        User u = new User();
        u.setId(rs.getLong("id_user"));
        u.setName(rs.getString("name"));
        u.setEmail(rs.getString("email"));
        u.setPassword(rs.getString("password"));
        u.setRole(rs.getString("role"));
        u.setActive(rs.getBoolean("is_active"));
        return u;
    };

    private final JdbcTemplate jdbc;

    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /** Lista todos los usuarios. */
    public List<User> findAll() {
        return jdbc.query("SELECT " + COLUMNS + " FROM users ORDER BY id_user", MAPPER);
    }

    /** Busca un usuario por email. */
    public Optional<User> findByEmail(String email) {
        List<User> list = jdbc.query(
                "SELECT " + COLUMNS + " FROM users WHERE email = ?", MAPPER, email);
        return list.stream().findFirst();
    }

    /** Busca un usuario por id. */
    public Optional<User> findById(Long id) {
        return jdbc.query("SELECT " + COLUMNS + " FROM users WHERE id_user = ?", MAPPER, id)
                .stream().findFirst();
    }

    /** Indica si ya existe un usuario con ese email. */
    public boolean existsByEmail(String email) {
        Integer n = jdbc.queryForObject("SELECT COUNT(*) FROM users WHERE email = ?", Integer.class, email);
        return n != null && n > 0;
    }

    /** Indica si otro usuario distinto al id indicado ya tiene el correo. */
    public boolean existsByEmailAndIdNot(String email, Long id) {
        Integer n = jdbc.queryForObject(
                "SELECT COUNT(*) FROM users WHERE email = ? AND id_user <> ?", Integer.class, email, id);
        return n != null && n > 0;
    }

    /** Actualiza los campos editables del perfil. */
    public int updateProfile(Long id, String name, String email, String password, String role) {
        return jdbc.update("UPDATE users SET name = ?, email = ?, password = ?, role = ? WHERE id_user = ?",
                name, email, password, role, id);
    }

    /** Inserta un usuario y devuelve el mismo objeto con su id generado. */
    public User save(User user) {
        KeyHolder keys = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO users (name, email, password, role, is_active) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole());
            ps.setBoolean(5, user.isActive());
            return ps;
        }, keys);
        if (keys.getKey() != null) {
            user.setId(keys.getKey().longValue());
        }
        return user;
    }

    /** Actualiza la contrasena de un usuario existente. */
    public int updatePassword(Long userId, String password) {
        return jdbc.update("UPDATE users SET password = ? WHERE id_user = ?", password, userId);
    }
}
