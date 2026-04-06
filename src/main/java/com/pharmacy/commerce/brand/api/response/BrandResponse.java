package com.pharmacy.commerce.brand.api.response;

import com.pharmacy.commerce.brand.infrastructure.persistence.entity.BrandEntity;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class BrandResponse {

    private Long id;
    private String name;
    private String slug;
    private String description;
    private String logoUrl;
    private Boolean active;
    private Instant createdAt;
    private Instant updatedAt;

    public static BrandResponse from(BrandEntity entity) {
        return BrandResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .slug(entity.getSlug())
                .description(entity.getDescription())
                .logoUrl(entity.getLogoUrl())
                .active(entity.getActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
