package com.sustainabilitytracker.sustainabilitytracker;

import com.sustainabilitytracker.sustainabilitytracker.dtos.request.LoginRequest;
import com.sustainabilitytracker.sustainabilitytracker.dtos.response.LoginResponse;
import com.sustainabilitytracker.sustainabilitytracker.entities.User;
import com.sustainabilitytracker.sustainabilitytracker.exceptions.BadRequestException;
import com.sustainabilitytracker.sustainabilitytracker.repositories.UserRepository;
import com.sustainabilitytracker.sustainabilitytracker.security.JwtTokenProvider;
import com.sustainabilitytracker.sustainabilitytracker.services.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;


import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private AuthService authService;

    @Test
    void login_WithCorrectCredentials_ShouldSucceed() {
        LoginRequest request = new LoginRequest("test@example.com", "password123");
        User user = User.builder()
                .id(1L)
                .email("test@example.com")
                .isActive(true)
                .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(jwtTokenProvider.generateAccessToken(user)).thenReturn("access.jwt.token");
        when(jwtTokenProvider.generateRefreshToken(user)).thenReturn("refresh.jwt.token");

        LoginResponse loginResponse = authService.login(request, response);

        assertNotNull(loginResponse);
        assertEquals("access.jwt.token", loginResponse.getAccessToken());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider).generateAccessToken(user);
        verify(response).addCookie(any());
    }

    @Test
    void login_WithWrongPassword_ShouldThrowBadCredentialsException() {
        LoginRequest request = new LoginRequest("test@example.com", "wrongpass");

        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager).authenticate(any());

        assertThrows(BadCredentialsException.class,
                () -> authService.login(request, response));
    }

    @Test
    void login_InactiveAccount_ShouldThrowException() {
        LoginRequest request = new LoginRequest("test@example.com", "password123");
        User user = User.builder().email("test@example.com").isActive(false).build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class,
                () -> authService.login(request, response));
    }
}