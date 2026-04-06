package com.pharmacy.commerce.cms.api.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateBannerRequest {

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
}
