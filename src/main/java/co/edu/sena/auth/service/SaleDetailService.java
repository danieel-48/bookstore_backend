package co.edu.sena.auth.service;

import co.edu.sena.auth.dto.SaleDetailRequest;
import co.edu.sena.auth.dto.SaleDetailResponse;
import co.edu.sena.auth.model.Book;
import co.edu.sena.auth.model.SaleDetail;
import co.edu.sena.auth.repository.BookRepository;
import co.edu.sena.auth.repository.SaleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

/**
 * Logica de negocio de detalles de venta (tabla sale_details).
 */
@Service
public class SaleDetailService {

    private final SaleRepository saleRepository;
    private final BookRepository bookRepository;

    public SaleDetailService(SaleRepository saleRepository, BookRepository bookRepository) {
        this.saleRepository = saleRepository;
        this.bookRepository = bookRepository;
    }

    /**
     * Agrega un libro a una venta: calcula el total con el precio actual del libro, descuenta el
     * stock y suma el total a la venta. Todo en una transaccion.
     *
     * @throws ResponseStatusException 404 si la venta o el libro no existen, 409 si el stock no alcanza
     */
    @Transactional
    public SaleDetailResponse create(SaleDetailRequest request) {
        if (saleRepository.findById(request.idSale()).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Venta no encontrada");
        }
        Book book = bookRepository.findById(request.idBook())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro no encontrado"));
        if (!bookRepository.decreaseStock(book.getId(), request.quantity())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Stock insuficiente para '" + book.getTitle() + "'");
        }
        BigDecimal total = book.getPrice().multiply(BigDecimal.valueOf(request.quantity()));
        long id = saleRepository.addDetail(request.idSale(), book.getId(), request.quantity(), total);
        saleRepository.addToTotal(request.idSale(), total);
        return toResponse(saleRepository.findDetailById(id).orElseThrow());
    }

    /** Lista detalles; si idSale es nulo devuelve todos. */
    public List<SaleDetailResponse> list(Long idSale) {
        return saleRepository.findDetails(idSale).stream().map(this::toResponse).toList();
    }

    /** @throws ResponseStatusException 404 si el detalle no existe */
    public SaleDetailResponse get(Long id) {
        return toResponse(saleRepository.findDetailById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Detalle no encontrado")));
    }

    private SaleDetailResponse toResponse(SaleDetail d) {
        return new SaleDetailResponse(d.getId(), d.getSaleId(), d.getBookId(), d.getQuantity(), d.getTotal());
    }
}
