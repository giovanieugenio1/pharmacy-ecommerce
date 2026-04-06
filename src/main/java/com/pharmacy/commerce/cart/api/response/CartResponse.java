package com.pharmacy.commerce.cart.api.response;

import com.pharmacy.commerce.cart.infrastructure.persistence.entity.CartEntity;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class CartResponse {

    private Long id;
    private String status;
    private List<CartItemResponse> items;
    private int itemCount;
    private BigDecimal total;

    public static CartResponse from(CartEntity cart) {
        List<CartItemResponse> items = cart.getItems().stream()
                .map(CartItemResponse::from)
                .toList();

        BigDecimal total = items.stream()
                .map(CartItemResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartResponse.builder()
                .id(cart.getId())
                .status(cart.getStatus())
                .items(items)
                .itemCount(items.size())
                .total(total)
                .build();
    }
}
