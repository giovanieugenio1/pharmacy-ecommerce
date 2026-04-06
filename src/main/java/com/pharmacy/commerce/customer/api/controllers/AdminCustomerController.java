package com.pharmacy.commerce.customer.api.controllers;

import com.pharmacy.commerce.customer.api.response.CustomerResponse;
import com.pharmacy.commerce.customer.application.services.CustomerService;
import com.pharmacy.commerce.shared.api.response.ApiResponse;
import com.pharmacy.commerce.shared.api.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/customers")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
@Tag(name = "Admin - Clientes", description = "Consulta de clientes")
@SecurityRequirement(name = "bearerAuth")
public class AdminCustomerController {

    private final CustomerService customerService;

    @GetMapping
    @Operation(summary = "Listar clientes (paginado)")
    public ResponseEntity<ApiResponse<PageResponse<CustomerResponse>>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String search) {
        return ResponseEntity.ok(ApiResponse.success(
                customerService.findAll(search, PageRequest.of(page, size, Sort.by("name").ascending()))));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID")
    public ResponseEntity<ApiResponse<CustomerResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(customerService.findById(id)));
    }
}
