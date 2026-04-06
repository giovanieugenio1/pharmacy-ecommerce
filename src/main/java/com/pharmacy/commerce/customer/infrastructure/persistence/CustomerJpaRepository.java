package com.pharmacy.commerce.customer.infrastructure.persistence;

import com.pharmacy.commerce.customer.infrastructure.persistence.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, Long>,
        JpaSpecificationExecutor<CustomerEntity> {

    Optional<CustomerEntity> findByEmail(String email);

    @Query("SELECT c FROM CustomerEntity c WHERE c.email = :email AND c.active = true")
    Optional<CustomerEntity> findActiveByEmail(@Param("email") String email);

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);
}
