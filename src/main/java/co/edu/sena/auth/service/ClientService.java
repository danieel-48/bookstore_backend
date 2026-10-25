package co.edu.sena.auth.service;

import co.edu.sena.auth.dto.ClientRequest;
import co.edu.sena.auth.dto.ClientResponse;
import co.edu.sena.auth.model.Client;
import co.edu.sena.auth.repository.ClientRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Logica de negocio de clientes (tabla clients).
 */
@Service
public class ClientService {

    /** Id que nunca existe, usado para validar duplicados al crear. */
    private static final long NO_ID = -1L;

    private final ClientRepository repository;

    public ClientService(ClientRepository repository) {
        this.repository = repository;
    }

    /**
     * Crea un cliente.
     *
     * @throws ResponseStatusException 409 si el email o el documento ya estan registrados
     */
    public ClientResponse create(ClientRequest request) {
        checkDuplicates(request, NO_ID);
        try {
            return toResponse(repository.save(toClient(request)));
        } catch (DuplicateKeyException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email o el documento ya estan registrados");
        }
    }

    /** Lista todos los clientes. */
    public List<ClientResponse> list() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    /** @throws ResponseStatusException 404 si el cliente no existe */
    public ClientResponse get(Long id) {
        return toResponse(repository.findById(id).orElseThrow(this::notFound));
    }

    /**
     * Reemplaza los datos de un cliente.
     *
     * @throws ResponseStatusException 404 si no existe, 409 si el email o el documento pertenecen a otro cliente
     */
    public ClientResponse update(Long id, ClientRequest request) {
        if (!repository.existsById(id)) {
            throw notFound();
        }
        checkDuplicates(request, id);
        try {
            repository.update(id, toClient(request));
        } catch (DuplicateKeyException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email o el documento ya estan registrados");
        }
        return get(id);
    }

    private void checkDuplicates(ClientRequest request, Long excludeId) {
        if (request.email() != null && repository.existsByEmailAndIdNot(request.email().trim(), excludeId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya esta registrado");
        }
        if (request.idNumber() != null && repository.existsByIdNumberAndIdNot(request.idNumber(), excludeId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El documento ya esta registrado");
        }
    }

    private ResponseStatusException notFound() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado");
    }

    /** Convierte el request en entidad; un email vacio se guarda como nulo para no chocar con la restriccion unica. */
    private Client toClient(ClientRequest r) {
        Client c = new Client();
        c.setIdNumber(r.idNumber());
        c.setName(r.name().trim());
        c.setEmail(r.email() == null || r.email().isBlank() ? null : r.email().trim());
        c.setPhone(r.phone());
        return c;
    }

    private ClientResponse toResponse(Client c) {
        return new ClientResponse(c.getId(), c.getIdNumber(), c.getName(), c.getEmail(), c.getPhone());
    }
}
