package com.pharmacy.commerce.pricing.infrastructure.persistence;

import com.pharmacy.commerce.pricing.infrastructure.persistence.entity.ProductPriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductPriceJpaRepository extends JpaRepository<ProductPriceEntity, Long> {

    @Query("SELECT pp FROM ProductPriceEntity pp JOIN FETCH pp.store WHERE pp.product.id = :productId AND pp.store.isMain = true AND pp.active = true")
    Optional<ProductPriceEntity> findMainStorePriceByProductId(@Param("productId") Long productId);

    @Query("SELECT pp FROM ProductPriceEntity pp JOIN FETCH pp.store WHERE pp.product.id IN :productIds AND pp.store.isMain = true AND pp.active = true")
    List<ProductPriceEntity> findMainStorePricesByProductIds(@Param("productIds") List<Long> productIds);

    @Query("SELECT pp FROM ProductPriceEntity pp JOIN FETCH pp.store JOIN FETCH pp.product WHERE pp.product.id = :productId AND pp.store.id = :storeId")
    Optional<ProductPriceEntity> findByProductIdAndStoreId(@Param("productId") Long productId, @Param("storeId") Long storeId);

    boolean existsByProductIdAndStoreId(Long productId, Long storeId);

    @Query("SELECT pp FROM ProductPriceEntity pp JOIN FETCH pp.store JOIN FETCH pp.product WHERE pp.product.id = :productId")
    List<ProductPriceEntity> findAllByProductId(@Param("productId") Long productId);
}
