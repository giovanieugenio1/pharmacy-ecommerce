package com.pharmacy.commerce.catalog.api.response;

import com.pharmacy.commerce.catalog.infrastructure.persistence.entity.ProductImageEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductImageResponse {

    private Long id;
    private String imageUrl;
    private String altText;
    private Integer displayOrder;
    private Boolean isPrimary;

    public static ProductImageResponse from(ProductImageEntity entity) {
        return ProductImageResponse.builder()
                .id(entity.getId())
                .imageUrl(entity.getImageUrl())
                .altText(entity.getAltText())
                .displayOrder(entity.getDisplayOrder())
                .isPrimary(entity.getIsPrimary())
                .build();
    }
}
