package com.pharmacy.commerce.order.api.controllers;

import com.pharmacy.commerce.order.api.request.CheckoutRequest;
import com.pharmacy.commerce.order.api.response.OrderResponse;
import com.pharmacy.commerce.order.application.services.OrderService;
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

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Pedidos", description = "Checkout e acompanhamento de pedidos do cliente")
public class CustomerOrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    @Operation(summary = "Finalizar compra (checkout)")
    public ResponseEntity<ApiResponse<OrderResponse>> checkout(
            Principal principal, @Valid @RequestBody CheckoutRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Pedido criado com sucesso", orderService.checkout(principal.getName(), request)));
    }

    @GetMapping
    @Operation(summary = "Listar meus pedidos")
    public ResponseEntity<ApiResponse<PageResponse<OrderResponse>>> listMyOrders(
            Principal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(
                orderService.listMyOrders(principal.getName(),
                        PageRequest.of(page, size, Sort.by("createdAt").descending()))));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar detalhes de um pedido")
    public ResponseEntity<ApiResponse<OrderResponse>> getMyOrder(Principal principal, @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(orderService.getMyOrder(principal.getName(), id)));
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancelar pedido")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(Principal principal, @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Pedido cancelado", orderService.cancelMyOrder(principal.getName(), id)));
    }
}
