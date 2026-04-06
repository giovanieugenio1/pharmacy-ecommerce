package com.pharmacy.commerce.brand.api.controllers;

import com.pharmacy.commerce.brand.api.response.BrandResponse;
import com.pharmacy.commerce.brand.application.services.BrandService;
import com.pharmacy.commerce.shared.api.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/brands")
@RequiredArgsConstructor
@Tag(name = "Marcas", description = "Listagem pública de marcas")
public class BrandController {

    private final BrandService brandService;

    @GetMapping
    @Operation(summary = "Listar marcas ativas")
    public ResponseEntity<ApiResponse<List<BrandResponse>>> findAllActive() {
        return ResponseEntity.ok(ApiResponse.success(brandService.findAllActive()));
    }

    @GetMapping("/{slug}")
    @Operation(summary = "Buscar marca por slug")
    public ResponseEntity<ApiResponse<BrandResponse>> findBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(ApiResponse.success(brandService.findBySlug(slug)));
    }
}
