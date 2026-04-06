package com.pharmacy.commerce.catalog.api.response;

import com.pharmacy.commerce.catalog.infrastructure.persistence.entity.ProductEntity;
import com.pharmacy.commerce.catalog.infrastructure.persistence.entity.ProductImageEntity;
import com.pharmacy.commerce.pricing.infrastructure.persistence.entity.ProductPriceEntity;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class ProductSummaryResponse {

    private Long id;
    private String name;
    private String slug;
    private String sku;
    private String shortDescription;
    private Long categoryId;
    private String categoryName;
    private Long brandId;
    private String brandName;
    private String primaryImageUrl;
    private BigDecimal price;
    private BigDecimal promotionalPrice;
    private BigDecimal effectivePrice;
    private Boolean promotionActive;
    private Boolean inStock;
    private Integer availableQuantity;
    private Boolean featured;
    private Boolean requiresPrescription;
    private Boolean controlledItem;
    private Boolean active;
    private Instant createdAt;

    public static ProductSummaryResponse from(ProductEntity entity) {
        return from(entity, null, null, null);
    }

    public static ProductSummaryResponse from(ProductEntity entity, ProductPriceEntity price,
                                               Boolean inStock, Integer availableQuantity) {
        String primaryImageUrl = entity.getImages().stream()
                .filter(ProductImageEntity::getIsPrimary)
                .findFirst()
                .map(ProductImageEntity::getImageUrl)
                .orElse(entity.getImages().isEmpty() ? null : entity.getImages().get(0).getImageUrl());

        return ProductSummaryResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .slug(entity.getSlug())
                .sku(entity.getSku())
                .shortDescription(entity.getShortDescription())
                .categoryId(entity.getCategory() != null ? entity.getCategory().getId() : null)
                .categoryName(entity.getCategory() != null ? entity.getCategory().getName() : null)
                .brandId(entity.getBrand() != null ? entity.getBrand().getId() : null)
                .brandName(entity.getBrand() != null ? entity.getBrand().getName() : null)
                .primaryImageUrl(primaryImageUrl)
                .price(price != null ? price.getPrice() : null)
                .promotionalPrice(price != null ? price.getPromotionalPrice() : null)
                .effectivePrice(price != null ? price.getEffectivePrice() : null)
                .promotionActive(price != null ? price.isPromotionActive() : null)
                .inStock(inStock)
                .availableQuantity(availableQuantity)
                .featured(entity.getFeatured())
                .requiresPrescription(entity.getRequiresPrescription())
                .controlledItem(entity.getControlledItem())
                .active(entity.getActive())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
