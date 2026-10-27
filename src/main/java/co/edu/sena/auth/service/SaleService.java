package co.edu.sena.auth.service;

import co.edu.sena.auth.dto.SaleRequest;
import co.edu.sena.auth.dto.SaleResponse;
import co.edu.sena.auth.model.Sale;
import co.edu.sena.auth.repository.ClientRepository;
import co.edu.sena.auth.repository.SaleRepository;
import co.edu.sena.auth.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Logica de negocio de ventas (tabla sales).
 */
@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    public SaleService(SaleRepository saleRepository, ClientRepository clientRepository,
                       UserRepository userRepository) {
        this.saleRepository = saleRepository;
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
    }

    /**
     * Crea una venta con total 0. Todos los campos (date, idClient, idUser) son obligatorios.
     *
     * @throws ResponseStatusException 404 si el cliente o el usuario no existen
     */
    public SaleResponse create(SaleRequest request) {
        if (!clientRepository.existsById(request.idClient())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado");
        }
        if (userRepository.findById(request.idUser()).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        long id = saleRepository.createSale(request.date(), request.idClient(), request.idUser());
        return toResponse(saleRepository.findById(id).orElseThrow());
    }

    /** Lista ventas filtrando opcionalmente por cliente y/o usuario. */
    public List<SaleResponse> list(Long idClient, Long idUser) {
        return saleRepository.findAll(idClient, idUser).stream().map(this::toResponse).toList();
    }

    /** @throws ResponseStatusException 404 si la venta no existe */
    public SaleResponse get(Long id) {
        return toResponse(saleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venta no encontrada")));
    }

    private SaleResponse toResponse(Sale s) {
        return new SaleResponse(s.getId(), s.getDate(), s.getTotal(), s.getClientId(), s.getUserId());
    }
}
