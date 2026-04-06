package com.pharmacy.commerce.customer.api.response;

import com.pharmacy.commerce.customer.infrastructure.persistence.entity.CustomerEntity;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class CustomerResponse {

    private Long id;
    private String name;
    private String email;
    private String cpf;
    private String phone;
    private LocalDate birthDate;
    private Boolean active;
    private List<AddressResponse> addresses;
    private Instant createdAt;

    public static CustomerResponse from(CustomerEntity entity) {
        return CustomerResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .cpf(entity.getCpf())
                .phone(entity.getPhone())
                .birthDate(entity.getBirthDate())
                .active(entity.getActive())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public static CustomerResponse fromWithAddresses(CustomerEntity entity) {
        List<AddressResponse> addresses = entity.getAddresses().stream()
                .filter(a -> a.getActive())
                .map(AddressResponse::from)
                .toList();
        CustomerResponse response = from(entity);
        response.setAddresses(addresses);
        return response;
    }
}
