package com.pharmacy.commerce.customer.api.controllers;

import com.pharmacy.commerce.customer.api.request.CreateAddressRequest;
import com.pharmacy.commerce.customer.api.request.RegisterCustomerRequest;
import com.pharmacy.commerce.customer.api.request.UpdateCustomerRequest;
import com.pharmacy.commerce.customer.api.response.AddressResponse;
import com.pharmacy.commerce.customer.api.response.CustomerResponse;
import com.pharmacy.commerce.customer.application.services.CustomerService;
import com.pharmacy.commerce.shared.api.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Registro e perfil do cliente")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/api/v1/customers/register")
    @Operation(summary = "Registrar novo cliente")
    public ResponseEntity<ApiResponse<CustomerResponse>> register(@Valid @RequestBody RegisterCustomerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Cadastro realizado com sucesso", customerService.register(request)));
    }

    @GetMapping("/api/v1/me")
    @PreAuthorize("hasRole('CUSTOMER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Buscar perfil do cliente autenticado")
    public ResponseEntity<ApiResponse<CustomerResponse>> getProfile(Principal principal) {
        return ResponseEntity.ok(ApiResponse.success(customerService.getProfile(principal.getName())));
    }

    @PutMapping("/api/v1/me")
    @PreAuthorize("hasRole('CUSTOMER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Atualizar perfil do cliente autenticado")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateProfile(
            Principal principal, @Valid @RequestBody UpdateCustomerRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Perfil atualizado", customerService.updateProfile(principal.getName(), request)));
    }

    @GetMapping("/api/v1/me/addresses")
    @PreAuthorize("hasRole('CUSTOMER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Listar endereços do cliente")
    public ResponseEntity<ApiResponse<List<AddressResponse>>> listAddresses(Principal principal) {
        return ResponseEntity.ok(ApiResponse.success(customerService.listAddresses(principal.getName())));
    }

    @PostMapping("/api/v1/me/addresses")
    @PreAuthorize("hasRole('CUSTOMER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Adicionar endereço")
    public ResponseEntity<ApiResponse<AddressResponse>> addAddress(
            Principal principal, @Valid @RequestBody CreateAddressRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Endereço adicionado", customerService.addAddress(principal.getName(), request)));
    }

    @PutMapping("/api/v1/me/addresses/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Atualizar endereço")
    public ResponseEntity<ApiResponse<AddressResponse>> updateAddress(
            Principal principal, @PathVariable Long id, @Valid @RequestBody CreateAddressRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Endereço atualizado", customerService.updateAddress(principal.getName(), id, request)));
    }

    @PatchMapping("/api/v1/me/addresses/{id}/default")
    @PreAuthorize("hasRole('CUSTOMER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Definir endereço como padrão")
    public ResponseEntity<ApiResponse<AddressResponse>> setDefault(Principal principal, @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Endereço padrão atualizado", customerService.setDefaultAddress(principal.getName(), id)));
    }

    @DeleteMapping("/api/v1/me/addresses/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Remover endereço")
    public ResponseEntity<ApiResponse<Void>> removeAddress(Principal principal, @PathVariable Long id) {
        customerService.removeAddress(principal.getName(), id);
        return ResponseEntity.ok(ApiResponse.success("Endereço removido", null));
    }
}
