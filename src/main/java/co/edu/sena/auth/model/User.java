package co.edu.sena.auth.model;

/**
 * Entidad que representa una fila de la tabla "users".
 */
public class User {

    /** Columna id_user. */
    private Long id;
    /** Columna name. */
    private String name;
    /** Columna email (unica). */
    private String email;
    /** Columna password (hash BCrypt). */
    private String password;
    /** Columna role: ADMIN o EMPLOYEE. */
    private String role;
    /** Columna is_active. */
    private boolean active;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
