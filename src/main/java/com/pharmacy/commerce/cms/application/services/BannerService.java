package com.pharmacy.commerce.cms.application.services;

import com.pharmacy.commerce.audit.application.services.AuditService;
import com.pharmacy.commerce.cms.api.request.CreateBannerRequest;
import com.pharmacy.commerce.cms.api.request.UpdateBannerRequest;
import com.pharmacy.commerce.cms.api.response.BannerResponse;
import com.pharmacy.commerce.cms.infrastructure.persistence.BannerJpaRepository;
import com.pharmacy.commerce.cms.infrastructure.persistence.entity.BannerEntity;
import com.pharmacy.commerce.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerService {

    private final BannerJpaRepository bannerRepository;
    private final AuditService auditService;

    @Transactional
    public BannerResponse create(CreateBannerRequest request) {
        BannerEntity entity = BannerEntity.builder()
                .title(request.getTitle())
                .subtitle(request.getSubtitle())
                .imageUrl(request.getImageUrl())
                .mobileImageUrl(request.getMobileImageUrl())
                .targetUrl(request.getTargetUrl())
                .position(request.getPosition().toUpperCase())
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                .startAt(request.getStartAt())
                .endAt(request.getEndAt())
                .build();
        BannerEntity saved = bannerRepository.save(entity);
        auditService.log(actor(), "ADMIN", "CREATE", "Banner", saved.getId());
        return BannerResponse.from(saved);
    }

    @Transactional
    public BannerResponse update(Long id, UpdateBannerRequest request) {
        BannerEntity entity = findById(id);
        if (request.getTitle() != null) entity.setTitle(request.getTitle());
        if (request.getSubtitle() != null) entity.setSubtitle(request.getSubtitle());
        if (request.getImageUrl() != null) entity.setImageUrl(request.getImageUrl());
        if (request.getMobileImageUrl() != null) entity.setMobileImageUrl(request.getMobileImageUrl());
        if (request.getTargetUrl() != null) entity.setTargetUrl(request.getTargetUrl());
        if (request.getPosition() != null) entity.setPosition(request.getPosition().toUpperCase());
        if (request.getDisplayOrder() != null) entity.setDisplayOrder(request.getDisplayOrder());
        if (request.getActive() != null) entity.setActive(request.getActive());
        if (request.getStartAt() != null) entity.setStartAt(request.getStartAt());
        if (request.getEndAt() != null) entity.setEndAt(request.getEndAt());
        BannerEntity saved = bannerRepository.save(entity);
        auditService.log(actor(), "ADMIN", "UPDATE", "Banner", id);
        return BannerResponse.from(saved);
    }

    @Transactional
    public void delete(Long id) {
        BannerEntity entity = findById(id);
        entity.setActive(false);
        bannerRepository.save(entity);
        auditService.log(actor(), "ADMIN", "DELETE", "Banner", id);
    }

    @Transactional(readOnly = true)
    public List<BannerResponse> findAll() {
        return bannerRepository.findAll().stream().map(BannerResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public BannerResponse findByIdResponse(Long id) {
        return BannerResponse.from(findById(id));
    }

    @Transactional(readOnly = true)
    public List<BannerResponse> findActiveByPosition(String position) {
        return bannerRepository.findActiveByPosition(position.toUpperCase(), LocalDateTime.now())
                .stream().map(BannerResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<BannerResponse> findAllActive() {
        return bannerRepository.findAllActive(LocalDateTime.now())
                .stream().map(BannerResponse::from).toList();
    }

    private BannerEntity findById(Long id) {
        return bannerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Banner não encontrado: " + id));
    }

    private String actor() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "system";
    }
}
