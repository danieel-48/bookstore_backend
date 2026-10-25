package co.edu.sena.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Un cliente (fila de la tabla clients).
 */
@Schema(description = "Cliente")
public record ClientResponse(
        @Schema(example = "1") Long idClient,
        @Schema(example = "1012345678") Long idNumber,
        @Schema(example = "Ana Ariza") String name,
        @Schema(example = "ana12345@test.com") String email,
        @Schema(example = "3001234567") Long phone) {
}
