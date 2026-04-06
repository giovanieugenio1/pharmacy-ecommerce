package com.pharmacy.commerce.auth.api.controllers;

import com.pharmacy.commerce.auth.api.request.LoginRequest;
import com.pharmacy.commerce.auth.api.response.LoginResponse;
import com.pharmacy.commerce.auth.application.services.AuthService;
import com.pharmacy.commerce.shared.api.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints para autenticação de usuários")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login de administrador", description = "Autentica um usuário administrador e retorna um token JWT")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login realizado com sucesso", response));
    }

    @PostMapping("/customer/login")
    @Operation(summary = "Login de cliente", description = "Autentica um cliente e retorna um token JWT")
    public ResponseEntity<ApiResponse<LoginResponse>> customerLogin(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.customerLogin(request);
        return ResponseEntity.ok(ApiResponse.success("Login realizado com sucesso", response));
    }
}
