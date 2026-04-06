package com.pharmacy.commerce.shared.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfValidator implements ConstraintValidator<ValidCpf, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true; // deixa @NotBlank cuidar disso
        }

        String cpf = value.replaceAll("[^\\d]", "");

        if (cpf.length() != 11) return false;

        // rejeita CPFs com todos os dígitos iguais (ex: 111.111.111-11)
        if (cpf.chars().distinct().count() == 1) return false;

        return verificaDigito(cpf, 9) && verificaDigito(cpf, 10);
    }

    private boolean verificaDigito(String cpf, int pos) {
        int soma = 0;
        for (int i = 0; i < pos; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (pos + 1 - i);
        }
        int resto = (soma * 10) % 11;
        if (resto == 10 || resto == 11) resto = 0;
        return resto == Character.getNumericValue(cpf.charAt(pos));
    }
}
