package com.pharmacy.commerce.inventory.api.response;

import com.pharmacy.commerce.inventory.infrastructure.persistence.entity.StoreInventoryEntity;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class InventoryResponse {

    private Long id;
    private Long productId;
    private String productName;
    private Long storeId;
    private String storeName;
    private Integer availableQuantity;
    private Integer reservedQuantity;
    private Integer physicalAvailable;
    private Integer minimumQuantity;
    private Boolean inStock;
    private Boolean active;
    private Instant updatedAt;

    public static InventoryResponse from(StoreInventoryEntity entity) {
        return InventoryResponse.builder()
                .id(entity.getId())
                .productId(entity.getProduct().getId())
                .productName(entity.getProduct().getName())
                .storeId(entity.getStore().getId())
                .storeName(entity.getStore().getName())
                .availableQuantity(entity.getAvailableQuantity())
                .reservedQuantity(entity.getReservedQuantity())
                .physicalAvailable(entity.getPhysicalAvailable())
                .minimumQuantity(entity.getMinimumQuantity())
                .inStock(entity.isInStock())
                .active(entity.getActive())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
