package com.pharmacy.commerce.cms.api.controllers;

import com.pharmacy.commerce.cms.api.response.HomeResponse;
import com.pharmacy.commerce.cms.application.services.HomeService;
import com.pharmacy.commerce.shared.api.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/home")
@RequiredArgsConstructor
@Tag(name = "Home", description = "Dados da página inicial")
public class HomeController {

    private final HomeService homeService;

    @GetMapping
    @Operation(summary = "Dados da home — banners, categorias e produtos em destaque")
    public ResponseEntity<ApiResponse<HomeResponse>> getHome() {
        return ResponseEntity.ok(ApiResponse.success(homeService.getHome()));
    }
}
