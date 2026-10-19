package co.edu.sena.auth.service;

import co.edu.sena.auth.dto.CartItemRequest;
import co.edu.sena.auth.dto.CartItemResponse;
import co.edu.sena.auth.dto.CartResponse;
import co.edu.sena.auth.model.Book;
import co.edu.sena.auth.model.User;
import co.edu.sena.auth.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Logica de negocio del carrito de compras.
 * La base de datos no tiene tabla de carrito, por lo que se guarda en memoria
 * (id de usuario -> id de libro -> cantidad) y se pierde al reiniciar la API.
 */
@Service
public class CartService {

    private final Map<Long, Map<Long, Integer>> carts = new ConcurrentHashMap<>();

    private final UserService userService;
    private final BookRepository bookRepository;

    public CartService(UserService userService, BookRepository bookRepository) {
        this.userService = userService;
        this.bookRepository = bookRepository;
    }

    /**
     * Agrega un libro al carrito del usuario (suma la cantidad si ya estaba).
     *
     * @throws ResponseStatusException 404 si el usuario o el libro no existen,
     *                                 403 si el usuario esta inactivo,
     *                                 409 si la cantidad total supera el stock
     */
    public CartResponse addItem(CartItemRequest req) {
        User user = userService.requireActiveUser(req.email());
        Book book = bookRepository.findById(req.bookId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro no encontrado"));
        Map<Long, Integer> cart = carts.computeIfAbsent(user.getId(), k -> new ConcurrentHashMap<>());
        synchronized (cart) {
            int newQuantity = cart.getOrDefault(book.getId(), 0) + req.quantity();
            if (newQuantity > book.getStock()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Stock insuficiente para '" + book.getTitle() + "' (disponible: " + book.getStock() + ")");
            }
            cart.put(book.getId(), newQuantity);
        }
        return buildResponse(cart);
    }

    /**
     * Devuelve el contenido del carrito del usuario (vacio si no ha agregado libros).
     *
     * @throws ResponseStatusException 404 si el usuario no existe, 403 si esta inactivo
     */
    public CartResponse getCart(String email) {
        User user = userService.requireActiveUser(email);
        return buildResponse(carts.getOrDefault(user.getId(), Map.of()));
    }

    /** Devuelve una copia de los items del carrito (id de libro -> cantidad). */
    Map<Long, Integer> getItems(Long userId) {
        Map<Long, Integer> cart = carts.get(userId);
        return cart == null ? Map.of() : new HashMap<>(cart);
    }

    /** Vacia el carrito del usuario. */
    void clear(Long userId) {
        carts.remove(userId);
    }

    private CartResponse buildResponse(Map<Long, Integer> cart) {
        List<CartItemResponse> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<Long, Integer> entry : new HashMap<>(cart).entrySet()) {
            Book book = bookRepository.findById(entry.getKey()).orElse(null);
            if (book == null) {
                continue; // el libro ya no existe en el catalogo
            }
            BigDecimal subtotal = book.getPrice().multiply(BigDecimal.valueOf(entry.getValue()));
            items.add(new CartItemResponse(book.getId(), book.getTitle(), entry.getValue(), book.getPrice(), subtotal));
            total = total.add(subtotal);
        }
        return new CartResponse(items, total);
    }
}
