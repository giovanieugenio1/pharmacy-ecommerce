package com.pharmacy.commerce.cart.api.response;

import com.pharmacy.commerce.cart.infrastructure.persistence.entity.CartItemEntity;
import com.pharmacy.commerce.catalog.infrastructure.persistence.entity.ProductImageEntity;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class CartItemResponse {

    private Long id;
    private Long productId;
    private String productName;
    private String productSlug;
    private String productImageUrl;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;

    public static CartItemResponse from(CartItemEntity item) {
        String imageUrl = item.getProduct().getImages().stream()
                .filter(ProductImageEntity::getIsPrimary)
                .findFirst()
                .map(ProductImageEntity::getImageUrl)
                .orElse(item.getProduct().getImages().isEmpty() ? null
                        : item.getProduct().getImages().get(0).getImageUrl());

        return CartItemResponse.builder()
                .id(item.getId())
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .productSlug(item.getProduct().getSlug())
                .productImageUrl(imageUrl)
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPriceSnapshot())
                .subtotal(item.getSubtotal())
                .build();
    }
}
