package com.pharmacy.commerce.order.api.response;

import com.pharmacy.commerce.order.infrastructure.persistence.entity.CustomerOrderEntity;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class OrderResponse {

    private Long id;
    private String orderNumber;
    private String status;
    private String paymentMethod;
    private String deliveryMethod;

    private BigDecimal subtotalAmount;
    private BigDecimal discountAmount;
    private BigDecimal shippingAmount;
    private BigDecimal totalAmount;

    private String addressSnapshotJson;
    private LocalDate estimatedDeliveryDate;
    private LocalDateTime deliveredAt;
    private String notes;
    private LocalDateTime createdAt;

    private List<OrderItemResponse> items;
    private List<OrderStatusHistoryResponse> statusHistory;

    public static OrderResponse from(CustomerOrderEntity order) {
        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .status(order.getStatus())
                .paymentMethod(order.getPaymentMethod())
                .deliveryMethod(order.getDeliveryMethod())
                .subtotalAmount(order.getSubtotalAmount())
                .discountAmount(order.getDiscountAmount())
                .shippingAmount(order.getShippingAmount())
                .totalAmount(order.getTotalAmount())
                .addressSnapshotJson(order.getAddressSnapshotJson())
                .estimatedDeliveryDate(order.getEstimatedDeliveryDate())
                .deliveredAt(order.getDeliveredAt())
                .notes(order.getNotes())
                .createdAt(order.getCreatedAt())
                .items(order.getItems().stream().map(OrderItemResponse::from).toList())
                .statusHistory(order.getStatusHistory().stream().map(OrderStatusHistoryResponse::from).toList())
                .build();
    }

    public static OrderResponse summary(CustomerOrderEntity order) {
        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .status(order.getStatus())
                .paymentMethod(order.getPaymentMethod())
                .deliveryMethod(order.getDeliveryMethod())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
