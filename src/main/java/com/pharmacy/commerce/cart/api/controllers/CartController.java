package com.pharmacy.commerce.cart.api.controllers;

import com.pharmacy.commerce.cart.api.request.AddCartItemRequest;
import com.pharmacy.commerce.cart.api.request.UpdateCartItemRequest;
import com.pharmacy.commerce.cart.api.response.CartResponse;
import com.pharmacy.commerce.cart.application.services.CartService;
import com.pharmacy.commerce.shared.api.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Carrinho", description = "Gerenciamento do carrinho de compras")
public class CartController {

    private final CartService cartService;

    @GetMapping
    @Operation(summary = "Visualizar carrinho")
    public ResponseEntity<ApiResponse<CartResponse>> getCart(Principal principal) {
        return ResponseEntity.ok(ApiResponse.success(cartService.getCart(principal.getName())));
    }

    @PostMapping("/items")
    @Operation(summary = "Adicionar item ao carrinho")
    public ResponseEntity<ApiResponse<CartResponse>> addItem(
            Principal principal, @Valid @RequestBody AddCartItemRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Item adicionado ao carrinho", cartService.addItem(principal.getName(), request)));
    }

    @PutMapping("/items/{itemId}")
    @Operation(summary = "Atualizar quantidade de um item")
    public ResponseEntity<ApiResponse<CartResponse>> updateItem(
            Principal principal, @PathVariable Long itemId, @Valid @RequestBody UpdateCartItemRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Item atualizado", cartService.updateItem(principal.getName(), itemId, request)));
    }

    @DeleteMapping("/items/{itemId}")
    @Operation(summary = "Remover item do carrinho")
    public ResponseEntity<ApiResponse<CartResponse>> removeItem(Principal principal, @PathVariable Long itemId) {
        return ResponseEntity.ok(ApiResponse.success("Item removido", cartService.removeItem(principal.getName(), itemId)));
    }

    @DeleteMapping
    @Operation(summary = "Limpar carrinho")
    public ResponseEntity<ApiResponse<Void>> clearCart(Principal principal) {
        cartService.clearCart(principal.getName());
        return ResponseEntity.ok(ApiResponse.success("Carrinho limpo", null));
    }
}
