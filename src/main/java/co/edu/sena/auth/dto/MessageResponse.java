package co.edu.sena.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Respuesta generica de exito que no expone datos del usuario.
 */
@Schema(description = "Respuesta generica de una operacion exitosa")
public record MessageResponse(
        @Schema(example = "true") boolean success,
        @Schema(example = "Login exitoso") String message) {

    /** Crea una respuesta exitosa con el mensaje indicado. */
    public static MessageResponse ok(String message) {
        return new MessageResponse(true, message);
    }
}
