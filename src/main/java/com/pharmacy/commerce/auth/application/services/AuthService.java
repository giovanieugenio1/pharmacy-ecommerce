package com.pharmacy.commerce.auth.application.services;

import com.pharmacy.commerce.audit.application.services.AuditService;
import com.pharmacy.commerce.auth.api.request.LoginRequest;
import com.pharmacy.commerce.auth.api.response.LoginResponse;
import com.pharmacy.commerce.auth.infrastructure.config.JwtTokenProvider;
import com.pharmacy.commerce.auth.infrastructure.persistence.AdminUserJpaRepository;
import com.pharmacy.commerce.auth.infrastructure.persistence.entity.AdminUserEntity;
import com.pharmacy.commerce.customer.infrastructure.persistence.CustomerJpaRepository;
import com.pharmacy.commerce.customer.infrastructure.persistence.entity.CustomerEntity;
import com.pharmacy.commerce.shared.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final AdminUserJpaRepository adminUserRepository;
    private final CustomerJpaRepository customerRepository;
    private final AuditService auditService;

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            String token = tokenProvider.generateToken(authentication);

            AdminUserEntity user = adminUserRepository.findActiveByEmailWithRole(request.getEmail())
                    .orElseThrow(() -> new UnauthorizedException("Usuário não encontrado"));

            auditService.log(user.getEmail(), "ADMIN", user.getId(), "LOGIN", "AdminUser", user.getId(), null, null, null, null);

            return LoginResponse.builder()
                    .token(token).type("Bearer")
                    .email(user.getEmail()).name(user.getName())
                    .role(user.getRole().getName())
                    .build();

        } catch (AuthenticationException e) {
            auditService.log(request.getEmail(), "ADMIN", null, "LOGIN_FAILED", "AdminUser", null, null, null, null, null);
            throw new UnauthorizedException("Email ou senha inválidos");
        }
    }

    @Transactional(readOnly = true)
    public LoginResponse customerLogin(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            String token = tokenProvider.generateToken(authentication);

            CustomerEntity customer = customerRepository.findActiveByEmail(request.getEmail())
                    .orElseThrow(() -> new UnauthorizedException("Cliente não encontrado"));

            auditService.log(customer.getEmail(), "CUSTOMER", customer.getId(), "LOGIN", "Customer", customer.getId(), null, null, null, null);

            return LoginResponse.builder()
                    .token(token).type("Bearer")
                    .email(customer.getEmail()).name(customer.getName())
                    .role("CUSTOMER")
                    .build();

        } catch (AuthenticationException e) {
            auditService.log(request.getEmail(), "CUSTOMER", null, "LOGIN_FAILED", "Customer", null, null, null, null, null);
            throw new UnauthorizedException("Email ou senha inválidos");
        }
    }
}
