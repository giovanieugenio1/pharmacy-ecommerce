package com.pharmacy.commerce.pricing.api.response;

import com.pharmacy.commerce.pricing.infrastructure.persistence.entity.ProductPriceEntity;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class PriceResponse {

    private Long id;
    private Long productId;
    private Long storeId;
    private String storeName;
    private BigDecimal price;
    private BigDecimal promotionalPrice;
    private Instant promotionStartAt;
    private Instant promotionEndAt;
    private Boolean promotionActive;
    private BigDecimal effectivePrice;
    private Boolean active;
    private Instant updatedAt;

    public static PriceResponse from(ProductPriceEntity entity) {
        return PriceResponse.builder()
                .id(entity.getId())
                .productId(entity.getProduct().getId())
                .storeId(entity.getStore().getId())
                .storeName(entity.getStore().getName())
                .price(entity.getPrice())
                .promotionalPrice(entity.getPromotionalPrice())
                .promotionStartAt(entity.getPromotionStartAt())
                .promotionEndAt(entity.getPromotionEndAt())
                .promotionActive(entity.isPromotionActive())
                .effectivePrice(entity.getEffectivePrice())
                .active(entity.getActive())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
