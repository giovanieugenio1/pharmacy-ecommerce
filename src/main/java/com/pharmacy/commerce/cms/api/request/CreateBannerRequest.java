package com.pharmacy.commerce.cms.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateBannerRequest {

    @NotBlank(message = "Título é obrigatório")
    private String title;

    private String subtitle;

    @NotBlank(message = "URL da imagem é obrigatória")
    private String imageUrl;

    private String mobileImageUrl;
    private String targetUrl;

    @NotBlank(message = "Posição é obrigatória")
    private String position;

    private Integer displayOrder;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
}
