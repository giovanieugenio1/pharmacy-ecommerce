package com.pharmacy.commerce.catalog.api.controllers;

import com.pharmacy.commerce.catalog.api.request.CreateProductRequest;
import com.pharmacy.commerce.catalog.api.request.UpdateProductRequest;
import com.pharmacy.commerce.catalog.api.response.ProductDetailResponse;
import com.pharmacy.commerce.catalog.api.response.ProductSummaryResponse;
import com.pharmacy.commerce.catalog.application.services.ProductService;
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
@RequestMapping("/api/v1/admin/products")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
@Tag(name = "Admin - Produtos", description = "Gerenciamento de produtos")
@SecurityRequirement(name = "bearerAuth")
public class AdminProductController {

    private final ProductService productService;

    @PostMapping
    @Operation(summary = "Criar produto")
    public ResponseEntity<ApiResponse<ProductDetailResponse>> create(@Valid @RequestBody CreateProductRequest request) {
        ProductDetailResponse response = productService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Produto criado com sucesso", response));
    }

    @GetMapping
    @Operation(summary = "Listar produtos (paginado, com filtros)")
    public ResponseEntity<ApiResponse<PageResponse<ProductSummaryResponse>>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) Boolean featured,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) String search) {
        PageResponse<ProductSummaryResponse> response = productService.findAll(
                active, featured, categoryId, brandId, search,
                PageRequest.of(page, size, Sort.by("name").ascending()));
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID")
    public ResponseEntity<ApiResponse<ProductDetailResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(productService.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar produto")
    public ResponseEntity<ApiResponse<ProductDetailResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Produto atualizado com sucesso", productService.update(id, request)));
    }

    @PatchMapping("/{id}/active")
    @Operation(summary = "Alternar status ativo/inativo do produto")
    public ResponseEntity<ApiResponse<ProductDetailResponse>> toggleActive(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Status do produto atualizado", productService.toggleActive(id)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desativar produto")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Produto desativado com sucesso", null));
    }
}
