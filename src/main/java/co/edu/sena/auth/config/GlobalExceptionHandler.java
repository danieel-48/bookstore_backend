package co.edu.sena.auth.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para estandarizar las respuestas de error.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String message;
        if (fieldError != null) {
            String field = fieldError.getField();
            String defaultMessage = fieldError.getDefaultMessage();
            if (defaultMessage != null && !defaultMessage.isBlank()) {
                if (defaultMessage.equalsIgnoreCase("no debe estar vacío")
                        || defaultMessage.equalsIgnoreCase("no debe estar vacio")
                        || defaultMessage.equalsIgnoreCase("no debe ser nulo")
                        || defaultMessage.equalsIgnoreCase("must not be blank")
                        || defaultMessage.equalsIgnoreCase("must not be empty")
                        || defaultMessage.equalsIgnoreCase("must not be null")) {
                    message = "El campo '" + field + "' no debe estar vacío";
                } else {
                    message = defaultMessage;
                }
            } else {
                message = "El campo '" + field + "' no debe estar vacío";
            }
        } else {
            message = "Datos invalidos";
        }

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("message", message);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex) {
        HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
        String errorName = status != null ? status.getReasonPhrase() : ex.getStatusCode().toString();

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", ex.getStatusCode().value());
        body.put("error", errorName);
        body.put("message", ex.getReason());

        return ResponseEntity.status(ex.getStatusCode()).body(body);
    }
}
