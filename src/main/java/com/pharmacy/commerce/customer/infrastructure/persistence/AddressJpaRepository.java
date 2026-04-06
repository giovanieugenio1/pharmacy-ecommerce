package com.pharmacy.commerce.customer.infrastructure.persistence;

import com.pharmacy.commerce.customer.infrastructure.persistence.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AddressJpaRepository extends JpaRepository<AddressEntity, Long> {

    List<AddressEntity> findAllByCustomerIdAndActiveTrueOrderByIsDefaultDescCreatedAtDesc(Long customerId);

    Optional<AddressEntity> findByIdAndCustomerId(Long id, Long customerId);

    @Modifying
    @Query("UPDATE AddressEntity a SET a.isDefault = false WHERE a.customer.id = :customerId AND a.id <> :excludeId")
    void clearDefaultExcept(@Param("customerId") Long customerId, @Param("excludeId") Long excludeId);
}
