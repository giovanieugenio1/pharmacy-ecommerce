package com.pharmacy.commerce.pricing.api.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class SetPriceRequest {

    @NotNull(message = "ID do produto é obrigatório")
    private Long productId;

    @NotNull(message = "ID da loja é obrigatório")
    private Long storeId;

    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
    private BigDecimal price;

    @DecimalMin(value = "0.01", message = "Preço promocional deve ser maior que zero")
    private BigDecimal promotionalPrice;

    private Instant promotionStartAt;

    private Instant promotionEndAt;
}
