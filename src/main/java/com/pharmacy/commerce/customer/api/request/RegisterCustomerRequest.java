package com.pharmacy.commerce.customer.api.request;

import com.pharmacy.commerce.shared.validation.ValidCpf;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterCustomerRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
    private String name;

    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail inválido")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, max = 100, message = "Senha deve ter entre 6 e 100 caracteres")
    private String password;

    @ValidCpf
    private String cpf;

    @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
    private String phone;

    private LocalDate birthDate;
}
