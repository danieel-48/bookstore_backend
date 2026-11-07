package co.edu.sena.auth.controller;

import co.edu.sena.auth.dto.SaleRequest;
import co.edu.sena.auth.dto.SaleResponse;
import co.edu.sena.auth.service.SaleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SaleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SaleService saleService;

    @InjectMocks
    private SaleController saleController;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(saleController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Test
    void testCreateSale_Success() throws Exception {
        LocalDateTime date = LocalDateTime.of(2026, 10, 25, 1, 40, 0);
        SaleRequest request = new SaleRequest(date, 2L, 5L);
        SaleResponse response = new SaleResponse(15L, date, BigDecimal.ZERO, 2L, 5L);

        when(saleService.create(any(SaleRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idSale").value(15L))
                .andExpect(jsonPath("$.total").value(0))
                .andExpect(jsonPath("$.idClient").value(2L))
                .andExpect(jsonPath("$.idUser").value(5L));
    }

    @Test
    void testListSales_Success() throws Exception {
        LocalDateTime date = LocalDateTime.of(2026, 10, 25, 1, 40, 0);
        SaleResponse response = new SaleResponse(15L, date, new BigDecimal("44.00"), 2L, 5L);

        when(saleService.list(null, null)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/sales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idSale").value(15L))
                .andExpect(jsonPath("$[0].total").value(44.00));
    }

    @Test
    void testGetSaleById_Success() throws Exception {
        LocalDateTime date = LocalDateTime.of(2026, 10, 25, 1, 40, 0);
        SaleResponse response = new SaleResponse(15L, date, new BigDecimal("44.00"), 2L, 5L);

        when(saleService.get(15L)).thenReturn(response);

        mockMvc.perform(get("/api/sales/15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idSale").value(15L))
                .andExpect(jsonPath("$.total").value(44.00));
    }
}

