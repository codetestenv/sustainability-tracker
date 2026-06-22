package com.sustainabilitytracker.sustainabilitytracker;

import com.sustainabilitytracker.sustainabilitytracker.controllers.AuthController;
import com.sustainabilitytracker.sustainabilitytracker.dtos.request.LoginRequest;
import com.sustainabilitytracker.sustainabilitytracker.dtos.response.LoginResponse;
import com.sustainabilitytracker.sustainabilitytracker.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Test
    void login_Success_Returns200() throws Exception {
        // Given
        LoginResponse loginResponse = LoginResponse.builder()
                .accessToken("fake-access-token")
                .tokenType("Bearer")
                .email("test@example.com")
                .fullName("Test User")
                .role("ADMIN")
                .build();

        when(authService.login(any(LoginRequest.class), any(HttpServletResponse.class)))
                .thenReturn(loginResponse);

        String json = """
                {
                    "email": "test@example.com",
                    "password": "password123"
                }
                """;

        // When & Then
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("fake-access-token"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }
}