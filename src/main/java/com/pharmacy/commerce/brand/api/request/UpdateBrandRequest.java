package com.pharmacy.commerce.brand.api.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateBrandRequest {

    @Size(min = 1, max = 255, message = "Nome deve ter entre 1 e 255 caracteres")
    private String name;

    @Size(max = 2000, message = "Descrição deve ter no máximo 2000 caracteres")
    private String description;

    @Size(max = 500, message = "URL do logo deve ter no máximo 500 caracteres")
    private String logoUrl;

    private Boolean active;
}
