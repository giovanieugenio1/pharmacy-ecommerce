package com.pharmacy.commerce.audit.application.services;

import com.pharmacy.commerce.audit.api.response.AuditLogResponse;
import com.pharmacy.commerce.audit.infrastructure.persistence.AuditLogJpaRepository;
import com.pharmacy.commerce.audit.infrastructure.persistence.entity.AuditLogEntity;
import com.pharmacy.commerce.shared.api.response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogJpaRepository auditLogRepository;

    /**
     * Registra uma entrada de auditoria de forma assíncrona para não bloquear a requisição principal.
     */
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(String userEmail, String userType, Long userId,
                    String action, String entityType, Long entityId,
                    String oldValue, String newValue,
                    String ipAddress, String userAgent) {
        try {
            AuditLogEntity entry = AuditLogEntity.builder()
                    .userEmail(userEmail)
                    .userType(userType)
                    .userId(userId)
                    .action(action)
                    .entityType(entityType)
                    .entityId(entityId)
                    .oldValue(oldValue)
                    .newValue(newValue)
                    .ipAddress(ipAddress)
                    .userAgent(userAgent)
                    .build();
            auditLogRepository.save(entry);
        } catch (Exception e) {
            log.error("Erro ao registrar auditoria: action={} entity={}/{}", action, entityType, entityId, e);
        }
    }

    /**
     * Conveniência — sem metadados de request (uso interno/sistêmico).
     */
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(String userEmail, String userType, String action, String entityType, Long entityId) {
        log(userEmail, userType, null, action, entityType, entityId, null, null, null, null);
    }

    @Transactional(readOnly = true)
    public PageResponse<AuditLogResponse> findAll(String userEmail, String action, String entityType, Pageable pageable) {
        return PageResponse.from(
                auditLogRepository.findWithFilters(userEmail, action, entityType, pageable)
                        .map(AuditLogResponse::from)
        );
    }
}
