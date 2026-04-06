package com.pharmacy.commerce.pricing.infrastructure.persistence.entity;

import com.pharmacy.commerce.catalog.infrastructure.persistence.entity.ProductEntity;
import com.pharmacy.commerce.store.infrastructure.persistence.entity.StoreEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "product_price")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPriceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private StoreEntity store;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "promotional_price", precision = 10, scale = 2)
    private BigDecimal promotionalPrice;

    @Column(name = "promotion_start_at")
    private Instant promotionStartAt;

    @Column(name = "promotion_end_at")
    private Instant promotionEndAt;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    public boolean isPromotionActive() {
        if (promotionalPrice == null) return false;
        Instant now = Instant.now();
        boolean startOk = promotionStartAt == null || !now.isBefore(promotionStartAt);
        boolean endOk = promotionEndAt == null || now.isBefore(promotionEndAt);
        return startOk && endOk;
    }

    public BigDecimal getEffectivePrice() {
        return isPromotionActive() ? promotionalPrice : price;
    }
}
