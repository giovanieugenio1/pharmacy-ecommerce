package com.pharmacy.commerce.catalog.api.response;

import com.pharmacy.commerce.catalog.infrastructure.persistence.entity.ProductEntity;
import com.pharmacy.commerce.inventory.infrastructure.persistence.entity.StoreInventoryEntity;
import com.pharmacy.commerce.pricing.infrastructure.persistence.entity.ProductPriceEntity;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
public class ProductDetailResponse {

    private Long id;
    private String name;
    private String slug;
    private String sku;
    private String shortDescription;
    private String fullDescription;
    private Long categoryId;
    private String categoryName;
    private Long brandId;
    private String brandName;
    private List<ProductImageResponse> images;
    private BigDecimal price;
    private BigDecimal promotionalPrice;
    private BigDecimal effectivePrice;
    private Boolean promotionActive;
    private Instant promotionStartAt;
    private Instant promotionEndAt;
    private Boolean inStock;
    private Integer availableQuantity;
    private Boolean featured;
    private Boolean requiresPrescription;
    private Boolean controlledItem;
    private Boolean active;
    private Instant createdAt;
    private Instant updatedAt;

    public static ProductDetailResponse from(ProductEntity entity) {
        return from(entity, null, null);
    }

    public static ProductDetailResponse from(ProductEntity entity,
                                              ProductPriceEntity price,
                                              StoreInventoryEntity inventory) {
        List<ProductImageResponse> images = entity.getImages().stream()
                .map(ProductImageResponse::from)
                .toList();

        return ProductDetailResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .slug(entity.getSlug())
                .sku(entity.getSku())
                .shortDescription(entity.getShortDescription())
                .fullDescription(entity.getFullDescription())
                .categoryId(entity.getCategory() != null ? entity.getCategory().getId() : null)
                .categoryName(entity.getCategory() != null ? entity.getCategory().getName() : null)
                .brandId(entity.getBrand() != null ? entity.getBrand().getId() : null)
                .brandName(entity.getBrand() != null ? entity.getBrand().getName() : null)
                .images(images)
                .price(price != null ? price.getPrice() : null)
                .promotionalPrice(price != null ? price.getPromotionalPrice() : null)
                .effectivePrice(price != null ? price.getEffectivePrice() : null)
                .promotionActive(price != null ? price.isPromotionActive() : null)
                .promotionStartAt(price != null ? price.getPromotionStartAt() : null)
                .promotionEndAt(price != null ? price.getPromotionEndAt() : null)
                .inStock(inventory != null ? inventory.isInStock() : null)
                .availableQuantity(inventory != null ? inventory.getPhysicalAvailable() : null)
                .featured(entity.getFeatured())
                .requiresPrescription(entity.getRequiresPrescription())
                .controlledItem(entity.getControlledItem())
                .active(entity.getActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
