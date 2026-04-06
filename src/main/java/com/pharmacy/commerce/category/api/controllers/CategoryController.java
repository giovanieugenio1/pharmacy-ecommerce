package com.pharmacy.commerce.category.api.controllers;

import com.pharmacy.commerce.category.api.response.CategoryResponse;
import com.pharmacy.commerce.category.application.services.CategoryService;
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
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Tag(name = "Categorias", description = "Listagem pública de categorias")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Listar categorias ativas")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> findAllActive() {
        return ResponseEntity.ok(ApiResponse.success(categoryService.findAllActive()));
    }

    @GetMapping("/{slug}")
    @Operation(summary = "Buscar categoria por slug")
    public ResponseEntity<ApiResponse<CategoryResponse>> findBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(ApiResponse.success(categoryService.findBySlug(slug)));
    }
}
