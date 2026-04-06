package com.pharmacy.commerce.brand.api.controllers;

import com.pharmacy.commerce.brand.api.request.CreateBrandRequest;
import com.pharmacy.commerce.brand.api.request.UpdateBrandRequest;
import com.pharmacy.commerce.brand.api.response.BrandResponse;
import com.pharmacy.commerce.brand.application.services.BrandService;
import com.pharmacy.commerce.shared.api.response.ApiResponse;
import com.pharmacy.commerce.shared.api.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/brands")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
@Tag(name = "Admin - Marcas", description = "Gerenciamento de marcas")
@SecurityRequirement(name = "bearerAuth")
public class AdminBrandController {

    private final BrandService brandService;

    @PostMapping
    @Operation(summary = "Criar marca")
    public ResponseEntity<ApiResponse<BrandResponse>> create(@Valid @RequestBody CreateBrandRequest request) {
        BrandResponse response = brandService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Marca criada com sucesso", response));
    }

    @GetMapping
    @Operation(summary = "Listar marcas (paginado)")
    public ResponseEntity<ApiResponse<PageResponse<BrandResponse>>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        PageResponse<BrandResponse> response = brandService.findAll(
                PageRequest.of(page, size, Sort.by("name").ascending()));
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar marca por ID")
    public ResponseEntity<ApiResponse<BrandResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(brandService.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar marca")
    public ResponseEntity<ApiResponse<BrandResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBrandRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Marca atualizada com sucesso", brandService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desativar marca")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        brandService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Marca desativada com sucesso", null));
    }
}
