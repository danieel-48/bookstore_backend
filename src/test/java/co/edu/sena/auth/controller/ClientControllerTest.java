package co.edu.sena.auth.controller;

import co.edu.sena.auth.dto.ClientRequest;
import co.edu.sena.auth.dto.ClientResponse;
import co.edu.sena.auth.service.ClientService;
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
class ClientControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(clientController).build();
    }

    @Test
    void testCreateClient_Success() throws Exception {
        ClientRequest request = new ClientRequest(1012345678L, "Ana Ariza", "ana12345@test.com", 3001234567L);
        ClientResponse response = new ClientResponse(2L, 1012345678L, "Ana Ariza", "ana12345@test.com", 3001234567L);

        when(clientService.create(any(ClientRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idClient").value(2L))
                .andExpect(jsonPath("$.idNumber").value(1012345678L))
                .andExpect(jsonPath("$.name").value("Ana Ariza"))
                .andExpect(jsonPath("$.email").value("ana12345@test.com"))
                .andExpect(jsonPath("$.phone").value(3001234567L));
    }

    @Test
    void testListClients_Success() throws Exception {
        ClientResponse response = new ClientResponse(2L, 1012345678L, "Ana Ariza", "ana12345@test.com", 3001234567L);
        when(clientService.list()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idClient").value(2L))
                .andExpect(jsonPath("$[0].name").value("Ana Ariza"));
    }

    @Test
    void testUpdateClient_Success() throws Exception {
        ClientRequest request = new ClientRequest(1012345678L, "Ana Ariza", "ana12345@test.com", 3009876543L);
        ClientResponse response = new ClientResponse(2L, 1012345678L, "Ana Ariza", "ana12345@test.com", 3009876543L);

        when(clientService.update(eq(2L), any(ClientRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/clients/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idClient").value(2L))
                .andExpect(jsonPath("$.phone").value(3009876543L));
    }
}

