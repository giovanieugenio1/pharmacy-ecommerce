package com.pharmacy.commerce.order.api.response;

import com.pharmacy.commerce.order.infrastructure.persistence.entity.OrderStatusHistoryEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class OrderStatusHistoryResponse {

    private Long id;
    private String previousStatus;
    private String newStatus;
    private String notes;
    private String changedBy;
    private LocalDateTime createdAt;

    public static OrderStatusHistoryResponse from(OrderStatusHistoryEntity h) {
        return OrderStatusHistoryResponse.builder()
                .id(h.getId())
                .previousStatus(h.getPreviousStatus())
                .newStatus(h.getNewStatus())
                .notes(h.getNotes())
                .changedBy(h.getChangedBy())
                .createdAt(h.getCreatedAt())
                .build();
    }
}
