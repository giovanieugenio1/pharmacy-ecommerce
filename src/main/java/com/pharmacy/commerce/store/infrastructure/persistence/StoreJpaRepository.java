package com.pharmacy.commerce.store.infrastructure.persistence;

import com.pharmacy.commerce.store.infrastructure.persistence.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreJpaRepository extends JpaRepository<StoreEntity, Long> {

    Optional<StoreEntity> findByIsMainTrue();

    Optional<StoreEntity> findBySlug(String slug);
}
