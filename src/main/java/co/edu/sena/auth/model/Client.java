package co.edu.sena.auth.model;

/**
 * Entidad que representa una fila de la tabla "clients".
 */
public class Client {

    /** Columna id_client. */
    private Long id;
    /** Columna id_number (documento de identidad, unico, puede ser nulo). */
    private Long idNumber;
    /** Columna name. */
    private String name;
    /** Columna email (unica, puede ser nula). */
    private String email;
    /** Columna phone (puede ser nula). */
    private Long phone;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdNumber() { return idNumber; }
    public void setIdNumber(Long idNumber) { this.idNumber = idNumber; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Long getPhone() { return phone; }
    public void setPhone(Long phone) { this.phone = phone; }
}
