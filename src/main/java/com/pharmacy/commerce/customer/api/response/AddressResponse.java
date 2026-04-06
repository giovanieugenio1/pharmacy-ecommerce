package com.pharmacy.commerce.customer.api.response;

import com.pharmacy.commerce.customer.infrastructure.persistence.entity.AddressEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressResponse {

    private Long id;
    private String label;
    private String recipientName;
    private String street;
    private String number;
    private String complement;
    private String neighborhood;
    private String city;
    private String state;
    private String zipCode;
    private Boolean isDefault;

    public static AddressResponse from(AddressEntity entity) {
        return AddressResponse.builder()
                .id(entity.getId())
                .label(entity.getLabel())
                .recipientName(entity.getRecipientName())
                .street(entity.getStreet())
                .number(entity.getNumber())
                .complement(entity.getComplement())
                .neighborhood(entity.getNeighborhood())
                .city(entity.getCity())
                .state(entity.getState())
                .zipCode(entity.getZipCode())
                .isDefault(entity.getIsDefault())
                .build();
    }
}
