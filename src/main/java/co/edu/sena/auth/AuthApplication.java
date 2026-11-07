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
     * permitiendo que Spring Boot las resuelva en application.properties sin hardcodear.
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
                            if ("DB_URL".equalsIgnoreCase(key)) {
                                if (val.startsWith("mysql://")) {
                                    val = "jdbc:" + val;
                                }
                                if (!val.contains("?")) {
                                    val = val + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
                                }
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

        // Si DB_URL no fue pasada de forma explicita pero si sus componentes, construirla dinamicamente
        if (System.getProperty("DB_URL") == null && System.getenv("DB_URL") == null) {
            String host = System.getProperty("DB_HOST", System.getenv().getOrDefault("DB_HOST", "localhost"));
            String port = System.getProperty("DB_PORT", System.getenv().getOrDefault("DB_PORT", "3306"));
            if (host.contains(":")) {
                String[] parts = host.split(":", 2);
                host = parts[0];
                if (parts.length > 1 && !parts[1].isBlank()) {
                    port = parts[1];
                }
            }
            String name = System.getProperty("DB_NAME", System.getenv().getOrDefault("DB_NAME", "bookstore"));
            String ssl = System.getProperty("DB_SSL", System.getenv().getOrDefault("DB_SSL", "false"));
            System.setProperty("DB_URL", "jdbc:mysql://" + host + ":" + port + "/" + name + "?useSSL=" + ssl + "&serverTimezone=UTC&allowPublicKeyRetrieval=true");
        }

        if (System.getProperty("PORT") == null && System.getenv("PORT") == null) {
            System.setProperty("PORT", "8080");
        }

        if (System.getProperty("FRONTEND_URL") == null && System.getenv("FRONTEND_URL") == null) {
            System.setProperty("FRONTEND_URL", "http://localhost:5173,http://127.0.0.1:5173");
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
