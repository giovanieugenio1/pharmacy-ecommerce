package com.pharmacy.commerce.audit.infrastructure.persistence;

import com.pharmacy.commerce.audit.infrastructure.persistence.entity.AuditLogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuditLogJpaRepository extends JpaRepository<AuditLogEntity, Long> {

    @Query("SELECT a FROM AuditLogEntity a WHERE " +
           "(:userEmail IS NULL OR a.userEmail = :userEmail) AND " +
           "(:action IS NULL OR a.action = :action) AND " +
           "(:entityType IS NULL OR a.entityType = :entityType) " +
           "ORDER BY a.createdAt DESC")
    Page<AuditLogEntity> findWithFilters(
            @Param("userEmail") String userEmail,
            @Param("action") String action,
            @Param("entityType") String entityType,
            Pageable pageable);
}
