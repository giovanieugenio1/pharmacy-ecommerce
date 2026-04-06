package com.pharmacy.commerce.inventory.api.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdjustInventoryRequest {

    @NotNull(message = "Quantidade do ajuste é obrigatória")
    private Integer quantity;

    private String reason;
}
