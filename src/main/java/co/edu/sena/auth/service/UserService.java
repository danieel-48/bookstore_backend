package co.edu.sena.auth.service;

import co.edu.sena.auth.dto.ProfileResponse;
import co.edu.sena.auth.model.User;
import co.edu.sena.auth.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Logica de negocio de usuarios: consulta de perfil y validacion de usuarios existentes.
 */
@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
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
