package com.pharmacy.commerce.shared.domain.enums;

public enum OrderStatus {
    CREATED,
    PENDING_PAYMENT,
    PAID,
    PREPARING,
    READY_FOR_PICKUP,
    OUT_FOR_DELIVERY,
    DELIVERED,
    CANCELED
}
