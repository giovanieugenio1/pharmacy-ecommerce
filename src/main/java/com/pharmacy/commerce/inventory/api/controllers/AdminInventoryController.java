package com.pharmacy.commerce.inventory.api.controllers;

import com.pharmacy.commerce.inventory.api.request.AdjustInventoryRequest;
import com.pharmacy.commerce.inventory.api.request.SetInventoryRequest;
import com.pharmacy.commerce.inventory.api.response.InventoryResponse;
import com.pharmacy.commerce.inventory.application.services.InventoryService;
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
@RequestMapping("/api/v1/admin/inventory")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
@Tag(name = "Admin - Estoque", description = "Gerenciamento de estoque por loja")
@SecurityRequirement(name = "bearerAuth")
public class AdminInventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    @Operation(summary = "Definir ou atualizar estoque de um produto em uma loja")
    public ResponseEntity<ApiResponse<InventoryResponse>> setInventory(@Valid @RequestBody SetInventoryRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Estoque atualizado com sucesso", inventoryService.setInventory(request)));
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Listar estoque de um produto em todas as lojas")
    public ResponseEntity<ApiResponse<List<InventoryResponse>>> findByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(ApiResponse.success(inventoryService.findByProductId(productId)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar registro de estoque por ID")
    public ResponseEntity<ApiResponse<InventoryResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(inventoryService.findById(id)));
    }

    @PatchMapping("/{id}/adjust")
    @Operation(summary = "Ajustar quantidade em estoque (positivo = entrada, negativo = saída)")
    public ResponseEntity<ApiResponse<InventoryResponse>> adjust(
            @PathVariable Long id,
            @Valid @RequestBody AdjustInventoryRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Estoque ajustado com sucesso", inventoryService.adjust(id, request)));
    }
}
