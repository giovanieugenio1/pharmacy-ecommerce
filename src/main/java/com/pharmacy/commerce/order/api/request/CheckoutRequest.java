package com.pharmacy.commerce.order.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckoutRequest {

    @NotNull(message = "ID do endereço de entrega é obrigatório")
    private Long addressId;

    @NotBlank(message = "Método de pagamento é obrigatório")
    private String paymentMethod;

    @NotBlank(message = "Método de entrega é obrigatório")
    private String deliveryMethod;

    private String notes;
}
