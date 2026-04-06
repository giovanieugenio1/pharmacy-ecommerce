package com.pharmacy.commerce.search.api.controllers;

import com.pharmacy.commerce.search.api.response.SearchResponse;
import com.pharmacy.commerce.search.application.services.SearchService;
import com.pharmacy.commerce.shared.api.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
@Tag(name = "Busca", description = "Busca global em produtos, categorias e marcas")
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    @Operation(summary = "Busca global", description = "Busca simultânea em produtos, categorias e marcas. Retorna até 10 resultados por tipo.")
    public ResponseEntity<ApiResponse<SearchResponse>> search(@RequestParam String q) {
        return ResponseEntity.ok(ApiResponse.success(searchService.search(q)));
    }
}
