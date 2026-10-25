package co.edu.sena.auth;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Punto de entrada de la API de autenticacion.
 * Usa la base de datos "bookstore" y su tabla existente "users".
 */
@SpringBootApplication
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

    /** Codificador BCrypt usado para guardar y verificar contrasenas. */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /** Informacion general de la documentacion Swagger (UI: /swagger-ui.html, JSON: /v3/api-docs). */
    @Bean
    public OpenAPI openApi() {
        return new OpenAPI().info(new Info()
                .title("Bookstore API")
                .version("1.0.0")
                .description("API de usuarios, catalogo de libros, clientes y ventas."));
    }
}
