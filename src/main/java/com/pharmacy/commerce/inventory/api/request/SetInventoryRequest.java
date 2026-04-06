package com.pharmacy.commerce.inventory.api.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SetInventoryRequest {

    @NotNull(message = "ID do produto é obrigatório")
    private Long productId;

    @NotNull(message = "ID da loja é obrigatório")
    private Long storeId;

    @NotNull(message = "Quantidade disponível é obrigatória")
    @Min(value = 0, message = "Quantidade não pode ser negativa")
    private Integer availableQuantity;

    @Min(value = 0, message = "Quantidade mínima não pode ser negativa")
    private Integer minimumQuantity = 0;
}
