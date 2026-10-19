package co.edu.sena.auth.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Datos de entrada para iniciar sesion.
 */
public record LoginRequest(
        @JsonAlias("Email") @NotBlank @Email String email,
        @JsonAlias("Password") @NotBlank String password) {
}
