package co.edu.sena.auth.model;

/**
 * Entidad que representa una fila de la tabla "clients".
 * Solo se mapean las columnas que usa la API (id_client, name, email).
 */
public class Client {

    /** Columna id_client. */
    private Long id;
    /** Columna name. */
    private String name;
    /** Columna email (unica). */
    private String email;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
