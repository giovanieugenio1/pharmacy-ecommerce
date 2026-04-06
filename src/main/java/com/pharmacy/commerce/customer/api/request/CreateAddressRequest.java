package com.pharmacy.commerce.customer.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateAddressRequest {

    @Size(max = 100, message = "Rótulo deve ter no máximo 100 caracteres")
    private String label;

    @NotBlank(message = "Nome do destinatário é obrigatório")
    @Size(max = 255)
    private String recipientName;

    @NotBlank(message = "Rua é obrigatória")
    @Size(max = 255)
    private String street;

    @NotBlank(message = "Número é obrigatório")
    @Size(max = 20)
    private String number;

    @Size(max = 100)
    private String complement;

    @NotBlank(message = "Bairro é obrigatório")
    @Size(max = 100)
    private String neighborhood;

    @NotBlank(message = "Cidade é obrigatória")
    @Size(max = 100)
    private String city;

    @NotBlank(message = "Estado é obrigatório")
    @Size(min = 2, max = 2, message = "Estado deve ter 2 caracteres (ex: SP)")
    private String state;

    @NotBlank(message = "CEP é obrigatório")
    @Pattern(regexp = "\\d{5}-?\\d{3}", message = "CEP inválido")
    private String zipCode;

    private Boolean isDefault = false;
}
