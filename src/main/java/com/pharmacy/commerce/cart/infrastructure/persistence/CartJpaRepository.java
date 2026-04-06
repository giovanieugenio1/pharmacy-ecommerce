package com.pharmacy.commerce.cart.infrastructure.persistence;

import com.pharmacy.commerce.cart.infrastructure.persistence.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartJpaRepository extends JpaRepository<CartEntity, Long> {

    @Query("SELECT c FROM CartEntity c LEFT JOIN FETCH c.items i LEFT JOIN FETCH i.product " +
           "WHERE c.customer.id = :customerId AND c.status = 'OPEN'")
    Optional<CartEntity> findOpenCartByCustomerId(@Param("customerId") Long customerId);
}
