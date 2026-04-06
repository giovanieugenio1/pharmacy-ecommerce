package com.pharmacy.commerce.order.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrderStatusRequest {

    @NotBlank(message = "Novo status é obrigatório")
    private String status;

    private String notes;
}
