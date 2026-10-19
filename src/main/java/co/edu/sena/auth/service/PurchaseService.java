package co.edu.sena.auth.service;

import co.edu.sena.auth.dto.PurchaseHistoryResponse;
import co.edu.sena.auth.dto.PurchaseItemResponse;
import co.edu.sena.auth.dto.PurchaseResponse;
import co.edu.sena.auth.model.Book;
import co.edu.sena.auth.model.Client;
import co.edu.sena.auth.model.SaleDetail;
import co.edu.sena.auth.model.User;
import co.edu.sena.auth.repository.BookRepository;
import co.edu.sena.auth.repository.ClientRepository;
import co.edu.sena.auth.repository.SaleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Logica de negocio de compras: pago del carrito e historial.
 */
@Service
public class PurchaseService {

    private final UserService userService;
    private final CartService cartService;
    private final BookRepository bookRepository;
    private final ClientRepository clientRepository;
    private final SaleRepository saleRepository;

    public PurchaseService(UserService userService, CartService cartService, BookRepository bookRepository,
                           ClientRepository clientRepository, SaleRepository saleRepository) {
        this.userService = userService;
        this.cartService = cartService;
        this.bookRepository = bookRepository;
        this.clientRepository = clientRepository;
        this.saleRepository = saleRepository;
    }

    /**
     * Compra todos los libros del carrito del usuario: registra la venta y sus detalles,
     * descuenta el stock y vacia el carrito. Todo ocurre en una transaccion: si algo falla,
     * no se guarda nada. El pago se asume aprobado (no hay pasarela de pago).
     *
     * @throws ResponseStatusException 404 si el usuario o un libro no existen, 403 si esta inactivo,
     *                                 400 si el carrito esta vacio, 409 si el stock no alcanza
     */
    @Transactional
    public PurchaseResponse buy(String email) {
        User user = userService.requireActiveUser(email);
        Map<Long, Integer> items = cartService.getItems(user.getId());
        if (items.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El carrito esta vacio");
        }

        // La venta exige un cliente: se reutiliza el que tenga el mismo email o se crea uno
        Client client = clientRepository.findByEmail(user.getEmail()).orElseGet(() -> {
            Client c = new Client();
            c.setName(user.getName());
            c.setEmail(user.getEmail());
            return clientRepository.save(c);
        });

        long saleId = saleRepository.createSale(client.getId(), user.getId());
        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<Long, Integer> item : items.entrySet()) {
            Book book = bookRepository.findById(item.getKey())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro no encontrado"));
            if (!bookRepository.decreaseStock(book.getId(), item.getValue())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Stock insuficiente para '" + book.getTitle() + "'");
            }
            BigDecimal lineTotal = book.getPrice().multiply(BigDecimal.valueOf(item.getValue()));
            saleRepository.addDetail(saleId, book.getId(), item.getValue(), lineTotal);
            total = total.add(lineTotal);
        }
        saleRepository.updateTotal(saleId, total);
        cartService.clear(user.getId());
        return new PurchaseResponse(true, "Compra realizada", saleId, total);
    }

    /**
     * Devuelve el historial de compras del usuario, de la mas reciente a la mas antigua.
     *
     * @throws ResponseStatusException 404 si el usuario no existe, 403 si esta inactivo
     */
    public List<PurchaseHistoryResponse> getHistory(String email) {
        User user = userService.requireActiveUser(email);
        Map<Long, List<SaleDetail>> detailsBySale = saleRepository.findDetailsByUserId(user.getId()).stream()
                .collect(Collectors.groupingBy(SaleDetail::getSaleId));
        return saleRepository.findByUserId(user.getId()).stream()
                .map(sale -> new PurchaseHistoryResponse(sale.getId(), sale.getDate(), sale.getTotal(),
                        detailsBySale.getOrDefault(sale.getId(), List.of()).stream()
                                .map(d -> new PurchaseItemResponse(d.getBookId(), d.getBookTitle(),
                                        d.getQuantity(), d.getTotal()))
                                .toList()))
                .toList();
    }
}
