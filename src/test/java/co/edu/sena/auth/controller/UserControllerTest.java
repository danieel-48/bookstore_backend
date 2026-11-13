package co.edu.sena.auth.controller;

import co.edu.sena.auth.dto.ProfileResponse;
import co.edu.sena.auth.dto.UpdateProfileRequest;
import co.edu.sena.auth.service.UserService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testListUsers_Success() throws Exception {
        ProfileResponse user = new ProfileResponse(1L, "Ana Ariza", "ana12345@test.com", "EMPLOYEE", true);
        when(userService.list()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].email").value("ana12345@test.com"));
    }

    @Test
    void testGetUserByEmailPath_MethodNotAllowed() throws Exception {
        mockMvc.perform(get("/api/users/ana12345@test.com"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void testGetUserProfileByParam_Success() throws Exception {
        ProfileResponse user = new ProfileResponse(1L, "Ana Ariza", "ana12345@test.com", "EMPLOYEE", true);
        when(userService.getProfile("ana12345@test.com")).thenReturn(user);

        mockMvc.perform(get("/api/users/profile").param("email", "ana12345@test.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Ana Ariza"));
    }

    @Test
    void testUpdateUser_Success() throws Exception {
        UpdateProfileRequest request = new UpdateProfileRequest("Ana Ariza", "ana12345@test.com", "123456", "ADMIN");
        ProfileResponse updatedUser = new ProfileResponse(1L, "Ana Ariza", "ana12345@test.com", "ADMIN", true);

        when(userService.updateProfile(eq(1L), any(UpdateProfileRequest.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }
}

