package com.pharmacy.commerce.customer.api.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateCustomerRequest {

    @Size(min = 1, max = 255, message = "Nome deve ter entre 1 e 255 caracteres")
    private String name;

    @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
    private String phone;

    private LocalDate birthDate;
}
