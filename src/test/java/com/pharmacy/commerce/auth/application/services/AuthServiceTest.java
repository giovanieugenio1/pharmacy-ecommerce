package com.pharmacy.commerce.auth.application.services;

import com.pharmacy.commerce.audit.application.services.AuditService;
import com.pharmacy.commerce.auth.api.request.LoginRequest;
import com.pharmacy.commerce.auth.api.response.LoginResponse;
import com.pharmacy.commerce.auth.infrastructure.config.JwtTokenProvider;
import com.pharmacy.commerce.auth.infrastructure.persistence.AdminUserJpaRepository;
import com.pharmacy.commerce.auth.infrastructure.persistence.entity.AdminUserEntity;
import com.pharmacy.commerce.auth.infrastructure.persistence.entity.RoleEntity;
import com.pharmacy.commerce.shared.exception.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService - Testes Unitários")
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private AdminUserJpaRepository adminUserRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private AuthService authService;

    private LoginRequest loginRequest;
    private AdminUserEntity adminUser;
    private RoleEntity role;

    @BeforeEach
    void setUp() {
        loginRequest = LoginRequest.builder()
                .email("admin@farmacia.com")
                .password("admin123")
                .build();

        role = RoleEntity.builder()
                .id(1L)
                .name("ADMIN")
                .description("Administrador")
                .active(true)
                .build();

        adminUser = AdminUserEntity.builder()
                .id(1L)
                .email("admin@farmacia.com")
                .name("Administrador")
                .password("$2a$10$encoded")
                .role(role)
                .active(true)
                .build();
    }

    @Test
    @DisplayName("Deve fazer login com sucesso quando credenciais são válidas")
    void shouldLoginSuccessfullyWithValidCredentials() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenProvider.generateToken(authentication))
                .thenReturn("jwt-token");
        when(adminUserRepository.findActiveByEmailWithRole(loginRequest.getEmail()))
                .thenReturn(Optional.of(adminUser));

        // Act
        LoginResponse response = authService.login(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals("Bearer", response.getType());
        assertEquals("admin@farmacia.com", response.getEmail());
        assertEquals("Administrador", response.getName());
        assertEquals("ADMIN", response.getRole());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenProvider).generateToken(authentication);
        verify(adminUserRepository).findActiveByEmailWithRole(loginRequest.getEmail());
    }

    @Test
    @DisplayName("Deve lançar UnauthorizedException quando credenciais são inválidas")
    void shouldThrowUnauthorizedExceptionWhenCredentialsAreInvalid() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // Act & Assert
        UnauthorizedException exception = assertThrows(
                UnauthorizedException.class,
                () -> authService.login(loginRequest)
        );

        assertEquals("Email ou senha inválidos", exception.getMessage());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoInteractions(tokenProvider);
        verifyNoInteractions(adminUserRepository);
    }

    @Test
    @DisplayName("Deve lançar UnauthorizedException quando usuário não é encontrado")
    void shouldThrowUnauthorizedExceptionWhenUserNotFound() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenProvider.generateToken(authentication))
                .thenReturn("jwt-token");
        when(adminUserRepository.findActiveByEmailWithRole(loginRequest.getEmail()))
                .thenReturn(Optional.empty());

        // Act & Assert
        UnauthorizedException exception = assertThrows(
                UnauthorizedException.class,
                () -> authService.login(loginRequest)
        );

        assertEquals("Usuário não encontrado", exception.getMessage());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenProvider).generateToken(authentication);
        verify(adminUserRepository).findActiveByEmailWithRole(loginRequest.getEmail());
    }
}
