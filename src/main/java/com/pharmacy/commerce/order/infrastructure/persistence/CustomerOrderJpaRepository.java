package com.pharmacy.commerce.order.infrastructure.persistence;

import com.pharmacy.commerce.order.infrastructure.persistence.entity.CustomerOrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerOrderJpaRepository extends JpaRepository<CustomerOrderEntity, Long>,
        JpaSpecificationExecutor<CustomerOrderEntity> {

    @Query("SELECT o FROM CustomerOrderEntity o JOIN FETCH o.store " +
           "LEFT JOIN FETCH o.items i LEFT JOIN FETCH i.product " +
           "WHERE o.id = :id AND o.customer.id = :customerId")
    Optional<CustomerOrderEntity> findByIdAndCustomerId(@Param("id") Long id, @Param("customerId") Long customerId);

    @Query("SELECT o FROM CustomerOrderEntity o WHERE o.customer.id = :customerId ORDER BY o.createdAt DESC")
    Page<CustomerOrderEntity> findAllByCustomerId(@Param("customerId") Long customerId, Pageable pageable);

    @Query("SELECT o FROM CustomerOrderEntity o JOIN FETCH o.customer JOIN FETCH o.store " +
           "LEFT JOIN FETCH o.items i LEFT JOIN FETCH i.product " +
           "WHERE o.id = :id")
    Optional<CustomerOrderEntity> findByIdWithDetails(@Param("id") Long id);

    Optional<CustomerOrderEntity> findByOrderNumber(String orderNumber);
}
