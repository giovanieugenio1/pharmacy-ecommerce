package com.pharmacy.commerce.auth.infrastructure.persistence;

import com.pharmacy.commerce.auth.infrastructure.persistence.entity.AdminUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AdminUserJpaRepository extends JpaRepository<AdminUserEntity, Long> {

    Optional<AdminUserEntity> findByEmail(String email);

    @Query("SELECT u FROM AdminUserEntity u JOIN FETCH u.role WHERE u.email = :email AND u.active = true")
    Optional<AdminUserEntity> findActiveByEmailWithRole(String email);

    boolean existsByEmail(String email);
}
