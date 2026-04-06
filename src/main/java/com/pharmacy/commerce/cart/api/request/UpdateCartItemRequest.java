package com.pharmacy.commerce.cart.api.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCartItemRequest {

    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 0, message = "Quantidade mínima é 0")
    private Integer quantity;
}
