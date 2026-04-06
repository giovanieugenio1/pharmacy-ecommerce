package com.pharmacy.commerce.catalog.api.controllers;

import com.pharmacy.commerce.catalog.api.response.ProductDetailResponse;
import com.pharmacy.commerce.catalog.api.response.ProductSummaryResponse;
import com.pharmacy.commerce.catalog.application.services.ProductService;
import com.pharmacy.commerce.shared.api.response.ApiResponse;
import com.pharmacy.commerce.shared.api.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Produtos", description = "Catálogo público de produtos")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Listar produtos ativos (paginado, com filtros)")
    public ResponseEntity<ApiResponse<PageResponse<ProductSummaryResponse>>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Boolean featured,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) String search) {
        PageResponse<ProductSummaryResponse> response = productService.findAll(
                true, featured, categoryId, brandId, search,
                PageRequest.of(page, size, Sort.by("name").ascending()));
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{slug}")
    @Operation(summary = "Buscar produto por slug")
    public ResponseEntity<ApiResponse<ProductDetailResponse>> findBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(ApiResponse.success(productService.findBySlug(slug)));
    }
}
