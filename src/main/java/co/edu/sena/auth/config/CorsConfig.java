package co.edu.sena.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Configuracion CORS: permite que el frontend consuma la API.
 * Las URLs permitidas se configuran a traves de la variable FRONTEND_URL en el archivo .env.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${app.cors.allowed-origins}")
    private String allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        Set<String> origins = new LinkedHashSet<>();
        for (String origin : allowedOrigins.split(",")) {
            String o = origin.trim();
            if (!o.isEmpty()) {
                if (o.endsWith("/")) {
                    o = o.substring(0, o.length() - 1);
                }
                origins.add(o);
                if (o.startsWith("http://")) {
                    origins.add(o.replaceFirst("http://", "https://"));
                } else if (o.startsWith("https://")) {
                    origins.add(o.replaceFirst("https://", "http://"));
                }
            }
        }

        registry.addMapping("/**")
                .allowedOrigins(origins.toArray(String[]::new))
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
