package com.pharmacy.commerce.pricing.api.controllers;

import com.pharmacy.commerce.pricing.api.request.SetPriceRequest;
import com.pharmacy.commerce.pricing.api.response.PriceResponse;
import com.pharmacy.commerce.pricing.application.services.PricingService;
import com.pharmacy.commerce.shared.api.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/prices")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
@Tag(name = "Admin - Preços", description = "Gerenciamento de preços por loja")
@SecurityRequirement(name = "bearerAuth")
public class AdminPricingController {

    private final PricingService pricingService;

    @PostMapping
    @Operation(summary = "Definir ou atualizar preço de um produto em uma loja")
    public ResponseEntity<ApiResponse<PriceResponse>> setPrice(@Valid @RequestBody SetPriceRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Preço definido com sucesso", pricingService.setPrice(request)));
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Listar preços de um produto em todas as lojas")
    public ResponseEntity<ApiResponse<List<PriceResponse>>> findByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(ApiResponse.success(pricingService.findByProductId(productId)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar preço por ID")
    public ResponseEntity<ApiResponse<PriceResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(pricingService.findById(id)));
    }

    @DeleteMapping("/{id}/promotion")
    @Operation(summary = "Remover promoção de um preço")
    public ResponseEntity<ApiResponse<PriceResponse>> removePromotion(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Promoção removida", pricingService.removePromotion(id)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desativar preço")
    public ResponseEntity<ApiResponse<Void>> deactivate(@PathVariable Long id) {
        pricingService.deactivate(id);
        return ResponseEntity.ok(ApiResponse.success("Preço desativado", null));
    }
}
