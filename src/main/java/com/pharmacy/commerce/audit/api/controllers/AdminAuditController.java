package com.pharmacy.commerce.audit.api.controllers;

import com.pharmacy.commerce.audit.api.response.AuditLogResponse;
import com.pharmacy.commerce.audit.application.services.AuditService;
import com.pharmacy.commerce.shared.api.response.ApiResponse;
import com.pharmacy.commerce.shared.api.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/audit")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin - Auditoria", description = "Consulta de logs de auditoria (somente ADMIN)")
public class AdminAuditController {

    private final AuditService auditService;

    @GetMapping
    @Operation(summary = "Listar logs de auditoria")
    public ResponseEntity<ApiResponse<PageResponse<AuditLogResponse>>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(required = false) String userEmail,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String entityType) {
        return ResponseEntity.ok(ApiResponse.success(
                auditService.findAll(userEmail, action, entityType,
                        PageRequest.of(page, size, Sort.by("createdAt").descending()))));
    }
}
