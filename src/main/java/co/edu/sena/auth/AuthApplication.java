package co.edu.sena.auth;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Punto de entrada de la API de autenticacion.
 * Carga variables de entorno desde el archivo .env en la raiz si existe.
 */
@SpringBootApplication
public class AuthApplication {

    public static void main(String[] args) {
        loadDotenv();
        SpringApplication.run(AuthApplication.class, args);
    }

    /**
     * Carga variables desde el archivo .env hacia las System Properties,
     * permitiendo que Spring Boot las resuelva en application.properties.
     */
    public static void loadDotenv() {
        File[] candidateFiles = new File[] {
                new File(".env"),
                new File(System.getProperty("user.dir", "."), ".env")
        };

        for (File file : candidateFiles) {
            if (file.exists() && file.isFile()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        line = line.trim();
                        if (line.isEmpty() || line.startsWith("#")) {
                            continue;
                        }
                        int eq = line.indexOf('=');
                        if (eq > 0) {
                            String key = line.substring(0, eq).trim();
                            String val = line.substring(eq + 1).trim();
                            if ((val.startsWith("\"") && val.endsWith("\"")) || (val.startsWith("'") && val.endsWith("'"))) {
                                val = val.substring(1, val.length() - 1);
                            }
                            if ("DB_URL".equalsIgnoreCase(key) && val.startsWith("mysql://")) {
                                val = "jdbc:" + val;
                            }
                            if (System.getProperty(key) == null && System.getenv(key) == null) {
                                System.setProperty(key, val);
                            }
                        }
                    }
                    break;
                } catch (IOException ignored) {
                }
            }
        }
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
