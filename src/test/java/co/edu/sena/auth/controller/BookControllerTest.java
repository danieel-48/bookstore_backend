package co.edu.sena.auth.controller;

import co.edu.sena.auth.dto.BookRequest;
import co.edu.sena.auth.dto.BookResponse;
import co.edu.sena.auth.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    void testCreateBook_Success() throws Exception {
        BookRequest request = new BookRequest("Harry Potter", "J.K Rowling", "Fantasia", new BigDecimal("22.00"), 20);
        BookResponse response = new BookResponse(7L, "Harry Potter", "J.K Rowling", "Fantasia", new BigDecimal("22.00"), 20);

        when(bookService.create(any(BookRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(7L))
                .andExpect(jsonPath("$.title").value("Harry Potter"))
                .andExpect(jsonPath("$.author").value("J.K Rowling"))
                .andExpect(jsonPath("$.category").value("Fantasia"))
                .andExpect(jsonPath("$.price").value(22.00))
                .andExpect(jsonPath("$.stock").value(20));
    }

    @Test
    void testUpdateBook_Success() throws Exception {
        BookRequest request = new BookRequest("Harry Potter", "J.K Rowling", "Fantasia", new BigDecimal("25.00"), 18);
        BookResponse response = new BookResponse(7L, "Harry Potter", "J.K Rowling", "Fantasia", new BigDecimal("25.00"), 18);

        when(bookService.update(eq(7L), any(BookRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/books/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7L))
                .andExpect(jsonPath("$.price").value(25.00))
                .andExpect(jsonPath("$.stock").value(18));
    }

    @Test
    void testListBooks_Success() throws Exception {
        BookResponse book = new BookResponse(7L, "Harry Potter", "J.K Rowling", "Fantasia", new BigDecimal("22.00"), 20);
        when(bookService.list(null, null, null)).thenReturn(List.of(book));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(7L))
                .andExpect(jsonPath("$[0].title").value("Harry Potter"));
    }

    @Test
    void testListBooksWithFilters_Success() throws Exception {
        BookResponse book = new BookResponse(7L, "Harry Potter", "J.K Rowling", "Fantasia", new BigDecimal("22.00"), 20);
        when(bookService.list("Fantasia", new BigDecimal("10"), new BigDecimal("50"))).thenReturn(List.of(book));

        mockMvc.perform(get("/api/books")
                        .param("category", "Fantasia")
                        .param("minPrice", "10")
                        .param("maxPrice", "50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(7L))
                .andExpect(jsonPath("$[0].category").value("Fantasia"));
    }
}

