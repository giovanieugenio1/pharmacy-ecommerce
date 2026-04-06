package com.pharmacy.commerce.inventory.infrastructure.persistence;

import com.pharmacy.commerce.inventory.infrastructure.persistence.entity.StoreInventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StoreInventoryJpaRepository extends JpaRepository<StoreInventoryEntity, Long> {

    @Query("SELECT si FROM StoreInventoryEntity si JOIN FETCH si.store WHERE si.product.id = :productId AND si.store.isMain = true")
    Optional<StoreInventoryEntity> findMainStoreInventoryByProductId(@Param("productId") Long productId);

    @Query("SELECT si FROM StoreInventoryEntity si JOIN FETCH si.store WHERE si.product.id IN :productIds AND si.store.isMain = true")
    List<StoreInventoryEntity> findMainStoreInventoriesByProductIds(@Param("productIds") List<Long> productIds);

    @Query("SELECT si FROM StoreInventoryEntity si JOIN FETCH si.store JOIN FETCH si.product WHERE si.product.id = :productId AND si.store.id = :storeId")
    Optional<StoreInventoryEntity> findByProductIdAndStoreId(@Param("productId") Long productId, @Param("storeId") Long storeId);

    @Query("SELECT si FROM StoreInventoryEntity si JOIN FETCH si.store JOIN FETCH si.product WHERE si.product.id = :productId")
    List<StoreInventoryEntity> findAllByProductId(@Param("productId") Long productId);
}
