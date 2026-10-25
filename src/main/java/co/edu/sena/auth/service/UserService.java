package co.edu.sena.auth.service;

import co.edu.sena.auth.dto.ProfileResponse;
import co.edu.sena.auth.dto.UpdateProfileRequest;
import co.edu.sena.auth.model.User;
import co.edu.sena.auth.repository.UserRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

/**
 * Logica de negocio de usuarios: consulta de perfil y validacion de usuarios existentes.
 */
@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Devuelve el perfil del usuario con ese email.
     *
     * @throws ResponseStatusException 404 si el usuario no existe
     */
    public ProfileResponse getProfile(String email) {
        return ProfileResponse.from(requireUser(email));
    }

    /**
     * Actualiza nombre y correo, y opcionalmente rol y contrasena.
     *
     * @throws ResponseStatusException 400 si el rol no esta permitido,
     *                                 404 si el usuario no existe,
     *                                 409 si el correo ya esta en uso
     */
    public ProfileResponse updateProfile(Long id, UpdateProfileRequest request) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        String email = request.email().trim().toLowerCase(Locale.ROOT);
        if (repository.existsByEmailAndIdNot(email, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya esta registrado");
        }

        String role = user.getRole();
        if (request.role() != null) {
            role = request.role().trim().toUpperCase(Locale.ROOT);
            if (!role.equals("ADMIN") && !role.equals("EMPLOYEE")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rol invalido (ADMIN o EMPLOYEE)");
            }
        }
        String password = request.password() == null
                ? user.getPassword()
                : passwordEncoder.encode(request.password());

        try {
            int updated = repository.updateProfile(id, request.name().trim(), email, password, role);
            if (updated != 1) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
            }
        } catch (DuplicateKeyException e) {
            // La restriccion unica de la BD protege tambien ante actualizaciones simultaneas.
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya esta registrado");
        }

        return repository.findById(user.getId())
                .map(ProfileResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    /**
     * Devuelve el usuario con ese email, exigiendo que exista y este activo.
     * Lo usan los servicios de carrito y compras.
     *
     * @throws ResponseStatusException 404 si no existe, 403 si esta inactivo
     */
    public User requireActiveUser(String email) {
        User user = requireUser(email);
        if (!user.isActive()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario inactivo");
        }
        return user;
    }

    private User requireUser(String email) {
        return repository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }
}
