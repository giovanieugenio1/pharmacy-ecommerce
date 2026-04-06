package com.pharmacy.commerce.catalog.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductImageRequest {

    @NotBlank(message = "URL da imagem é obrigatória")
    @Size(max = 500, message = "URL deve ter no máximo 500 caracteres")
    private String imageUrl;

    @Size(max = 255, message = "Texto alternativo deve ter no máximo 255 caracteres")
    private String altText;

    private Integer displayOrder = 0;

    private Boolean isPrimary = false;
}
