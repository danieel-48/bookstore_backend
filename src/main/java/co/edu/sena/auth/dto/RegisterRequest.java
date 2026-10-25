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
        @JsonAlias("Name") @NotBlank @Size(max = 150) String name,
        @JsonAlias("Email") @NotBlank @Email @Size(max = 100) String email,
        @JsonAlias("Password") @NotBlank @Size(min = 6, max = 72, message = "La contraseña debe tener al menos 6 caracteres") String password,
        @JsonAlias("Role") String role) {
}
