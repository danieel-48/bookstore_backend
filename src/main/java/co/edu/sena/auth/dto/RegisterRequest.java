package co.edu.sena.auth.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Datos de entrada para registrar un usuario.
 * Si "role" es nulo se asigna EMPLOYEE (valor por defecto de la tabla).
 */
public record RegisterRequest(
        @JsonAlias("Name")
        @NotBlank(message = "El campo 'name' no debe estar vacío")
        @Size(max = 150, message = "El campo 'name' no debe superar los 150 caracteres")
        String name,

        @JsonAlias("Email")
        @NotBlank(message = "El campo 'email' no debe estar vacío")
        @Email(message = "El campo 'email' debe tener un formato válido")
        @Size(max = 100, message = "El campo 'email' no debe superar los 100 caracteres")
        String email,

        @JsonAlias("Password")
        @NotBlank(message = "El campo 'password' no debe estar vacío")
        @Size(min = 6, max = 72, message = "La contraseña debe tener al menos 6 caracteres")
        String password,

        @JsonAlias("Role")
        String role) {
}
