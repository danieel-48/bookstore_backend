package co.edu.sena.auth.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Datos de entrada para iniciar sesion.
 */
public record LoginRequest(
        @JsonAlias("Email")
        @NotBlank(message = "El campo 'email' no debe estar vacío")
        @Email(message = "El campo 'email' debe tener un formato válido")
        String email,

        @JsonAlias("Password")
        @NotBlank(message = "El campo 'password' no debe estar vacío")
        String password) {
}
