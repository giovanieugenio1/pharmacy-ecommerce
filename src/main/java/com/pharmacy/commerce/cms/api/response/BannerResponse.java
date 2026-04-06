package com.pharmacy.commerce.cms.api.response;

import com.pharmacy.commerce.cms.infrastructure.persistence.entity.BannerEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BannerResponse {

    private Long id;
    private String title;
    private String subtitle;
    private String imageUrl;
    private String mobileImageUrl;
    private String targetUrl;
    private String position;
    private Integer displayOrder;
    private Boolean active;
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    public static BannerResponse from(BannerEntity entity) {
        return BannerResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .subtitle(entity.getSubtitle())
                .imageUrl(entity.getImageUrl())
                .mobileImageUrl(entity.getMobileImageUrl())
                .targetUrl(entity.getTargetUrl())
                .position(entity.getPosition())
                .displayOrder(entity.getDisplayOrder())
                .active(entity.getActive())
                .startAt(entity.getStartAt())
                .endAt(entity.getEndAt())
                .build();
    }
}
