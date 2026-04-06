package com.pharmacy.commerce.cms.api.controllers;

import com.pharmacy.commerce.cms.api.request.CreateBannerRequest;
import com.pharmacy.commerce.cms.api.request.UpdateBannerRequest;
import com.pharmacy.commerce.cms.api.response.BannerResponse;
import com.pharmacy.commerce.cms.application.services.BannerService;
import com.pharmacy.commerce.shared.api.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/banners")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin - Banners", description = "Gestão de banners e CMS")
public class AdminBannerController {

    private final BannerService bannerService;

    @PostMapping
    @Operation(summary = "Criar banner")
    public ResponseEntity<ApiResponse<BannerResponse>> create(@Valid @RequestBody CreateBannerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Banner criado", bannerService.create(request)));
    }

    @GetMapping
    @Operation(summary = "Listar todos os banners")
    public ResponseEntity<ApiResponse<List<BannerResponse>>> findAll() {
        return ResponseEntity.ok(ApiResponse.success(bannerService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar banner por ID")
    public ResponseEntity<ApiResponse<BannerResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(bannerService.findByIdResponse(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar banner")
    public ResponseEntity<ApiResponse<BannerResponse>> update(
            @PathVariable Long id, @RequestBody UpdateBannerRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Banner atualizado", bannerService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desativar banner")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        bannerService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Banner desativado", null));
    }
}
