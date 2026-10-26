package com.codesentinel.backend;

import com.codesentinel.backend.dto.AuthDTOs.*;
import com.codesentinel.backend.model.User;
import com.codesentinel.backend.repository.UserRepository;
import com.codesentinel.backend.security.JwtTokenProvider;
import com.codesentinel.backend.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider tokenProvider;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userRepository, passwordEncoder, tokenProvider);
    }

    @Test
    void testRegisterSuccess() {
        RegisterRequest request = new RegisterRequest("dev@example.com", "password123", "Dev Engineer", "gh_token");

        when(userRepository.existsByEmail("dev@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");

        User savedUser = new User("dev@example.com", "hashedPassword", "Dev Engineer");
        savedUser.setId("u-12345");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(tokenProvider.generateAccessToken(eq("u-12345"), eq("dev@example.com"))).thenReturn("access-token-xyz");
        when(tokenProvider.generateRefreshToken(eq("u-12345"))).thenReturn("refresh-token-xyz");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("dev@example.com", response.email());
        assertEquals("access-token-xyz", response.accessToken());
        assertEquals("refresh-token-xyz", response.refreshToken());
    }

    @Test
    void testRegisterDuplicateEmailThrows() {
        RegisterRequest request = new RegisterRequest("existing@example.com", "password123", "Dev Engineer", null);
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authService.register(request));
    }

    @Test
    void testLoginInvalidCredentialsThrows() {
        LoginRequest request = new LoginRequest("dev@example.com", "wrongpass");
        when(userRepository.findByEmail("dev@example.com")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> authService.login(request));
    }
}
