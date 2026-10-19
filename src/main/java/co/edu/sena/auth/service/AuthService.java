package co.edu.sena.auth.service;

import co.edu.sena.auth.dto.LoginRequest;
import co.edu.sena.auth.dto.RegisterRequest;
import co.edu.sena.auth.model.User;
import co.edu.sena.auth.repository.UserRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * Logica de negocio de autenticacion: registro e inicio de sesion.
 */
@Service
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public AuthService(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    /**
     * Registra un nuevo usuario activo con la contrasena cifrada.
     *
     * @throws ResponseStatusException 409 si el email ya existe, 400 si el rol es invalido
     */
    public void register(RegisterRequest req) {
        String email = req.email().trim().toLowerCase();
        if (repository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya esta registrado");
        }
        String role = req.role() == null ? "EMPLOYEE" : req.role().toUpperCase();
        if (!role.equals("ADMIN") && !role.equals("EMPLOYEE")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rol invalido (ADMIN o EMPLOYEE)");
        }
        User user = new User();
        user.setName(req.name().trim());
        user.setEmail(email);
        user.setPassword(encoder.encode(req.password()));
        user.setRole(role);
        user.setActive(true);
        try {
            repository.save(user);
        } catch (DuplicateKeyException e) {
            // Peticiones simultaneas: la restriccion unica de la BD tambien debe responder 409
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya esta registrado");
        }
    }

    /**
     * Valida las credenciales del usuario.
     *
     * @throws ResponseStatusException 401 si son incorrectas, 403 si el usuario esta inactivo
     */
    public void login(LoginRequest req) {
        User user = repository.findByEmail(req.email().trim().toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales invalidas"));
        String storedPassword = user.getPassword();
        boolean legacyPlaintext = storedPassword != null
                && !storedPassword.startsWith("$2")
                && MessageDigest.isEqual(
                        req.password().getBytes(StandardCharsets.UTF_8),
                        storedPassword.getBytes(StandardCharsets.UTF_8));
        boolean passwordMatches = storedPassword != null
                && (storedPassword.startsWith("$2")
                        ? encoder.matches(req.password(), storedPassword)
                        : legacyPlaintext);
        if (!passwordMatches) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales invalidas");
        }
        if (!user.isActive()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario inactivo");
        }
        if (legacyPlaintext) {
            int updated = repository.updatePassword(user.getId(), encoder.encode(req.password()));
            if (updated != 1) {
                throw new IllegalStateException("No se pudo actualizar la contrasena heredada");
            }
        }
    }
}
