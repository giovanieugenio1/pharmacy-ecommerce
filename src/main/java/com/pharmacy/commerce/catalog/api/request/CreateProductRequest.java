package com.pharmacy.commerce.catalog.api.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CreateProductRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
    private String name;

    @Size(max = 100, message = "SKU deve ter no máximo 100 caracteres")
    private String sku;

    private Long categoryId;

    private Long brandId;

    @Size(max = 500, message = "Descrição curta deve ter no máximo 500 caracteres")
    private String shortDescription;

    private String fullDescription;

    private Boolean featured = false;

    private Boolean requiresPrescription = false;

    private Boolean controlledItem = false;

    @Valid
    private List<ProductImageRequest> images = new ArrayList<>();
}
