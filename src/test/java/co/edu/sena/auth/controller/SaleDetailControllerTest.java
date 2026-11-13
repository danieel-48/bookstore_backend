package co.edu.sena.auth.controller;

import co.edu.sena.auth.dto.SaleDetailRequest;
import co.edu.sena.auth.dto.SaleDetailResponse;
import co.edu.sena.auth.service.SaleDetailService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SaleDetailControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SaleDetailService saleDetailService;

    @InjectMocks
    private SaleDetailController saleDetailController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(saleDetailController).build();
    }

    @Test
    void testCreateSaleDetail_Success() throws Exception {
        SaleDetailRequest request = new SaleDetailRequest(15L, 7L, 2);
        SaleDetailResponse response = new SaleDetailResponse(31L, 15L, 7L, 2, new BigDecimal("44.00"));

        when(saleDetailService.create(any(SaleDetailRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/sale-details")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idDetail").value(31L))
                .andExpect(jsonPath("$.idSale").value(15L))
                .andExpect(jsonPath("$.idBook").value(7L))
                .andExpect(jsonPath("$.quantity").value(2))
                .andExpect(jsonPath("$.total").value(44.00));
    }

    @Test
    void testListSaleDetails_Success() throws Exception {
        SaleDetailResponse response = new SaleDetailResponse(31L, 15L, 7L, 2, new BigDecimal("44.00"));

        when(saleDetailService.list()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/sale-details"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idDetail").value(31L))
                .andExpect(jsonPath("$[0].quantity").value(2));
    }

    @Test
    void testGetSaleDetailById_NotFound() throws Exception {
        mockMvc.perform(get("/api/sale-details/31"))
                .andExpect(status().isNotFound());
    }

}

