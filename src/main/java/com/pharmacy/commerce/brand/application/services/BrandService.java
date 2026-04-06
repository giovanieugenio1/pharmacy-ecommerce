package com.pharmacy.commerce.brand.application.services;

import com.pharmacy.commerce.audit.application.services.AuditService;
import com.pharmacy.commerce.brand.api.request.CreateBrandRequest;
import com.pharmacy.commerce.brand.api.request.UpdateBrandRequest;
import com.pharmacy.commerce.brand.api.response.BrandResponse;
import com.pharmacy.commerce.brand.infrastructure.persistence.BrandJpaRepository;
import com.pharmacy.commerce.brand.infrastructure.persistence.entity.BrandEntity;
import com.pharmacy.commerce.shared.api.response.PageResponse;
import com.pharmacy.commerce.shared.exception.BusinessException;
import com.pharmacy.commerce.shared.exception.ResourceNotFoundException;
import com.pharmacy.commerce.shared.util.SlugUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandJpaRepository repository;
    private final AuditService auditService;

    @Transactional
    public BrandResponse create(CreateBrandRequest request) {
        String slug = SlugUtils.toSlug(request.getName());

        if (repository.existsBySlug(slug)) {
            throw new BusinessException("Já existe uma marca com o nome '" + request.getName() + "'");
        }

        BrandEntity entity = BrandEntity.builder()
                .name(request.getName())
                .slug(slug)
                .description(request.getDescription())
                .logoUrl(request.getLogoUrl())
                .active(true)
                .build();

        BrandEntity saved = repository.save(entity);
        auditService.log(actor(), "ADMIN", "CREATE", "Brand", saved.getId());
        return BrandResponse.from(saved);
    }

    @Transactional
    public BrandResponse update(Long id, UpdateBrandRequest request) {
        BrandEntity entity = findEntityById(id);

        if (request.getName() != null && !request.getName().equals(entity.getName())) {
            String newSlug = SlugUtils.toSlug(request.getName());
            if (repository.existsBySlugAndIdNot(newSlug, id)) {
                throw new BusinessException("Já existe uma marca com o nome '" + request.getName() + "'");
            }
            entity.setName(request.getName());
            entity.setSlug(newSlug);
        }

        if (request.getDescription() != null) entity.setDescription(request.getDescription());
        if (request.getLogoUrl() != null) entity.setLogoUrl(request.getLogoUrl());
        if (request.getActive() != null) entity.setActive(request.getActive());

        BrandEntity saved = repository.save(entity);
        auditService.log(actor(), "ADMIN", "UPDATE", "Brand", id);
        return BrandResponse.from(saved);
    }

    @Transactional
    public void delete(Long id) {
        BrandEntity entity = findEntityById(id);
        entity.setActive(false);
        repository.save(entity);
        auditService.log(actor(), "ADMIN", "DELETE", "Brand", id);
    }

    @Transactional(readOnly = true)
    public PageResponse<BrandResponse> findAll(Pageable pageable) {
        return PageResponse.from(repository.findAll(pageable).map(BrandResponse::from));
    }

    @Transactional(readOnly = true)
    public List<BrandResponse> findAllActive() {
        return repository.findAllByActiveTrueOrderByNameAsc().stream().map(BrandResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public BrandResponse findBySlug(String slug) {
        return BrandResponse.from(repository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Marca não encontrada: " + slug)));
    }

    @Transactional(readOnly = true)
    public BrandResponse findById(Long id) {
        return BrandResponse.from(findEntityById(id));
    }

    private BrandEntity findEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Marca não encontrada: " + id));
    }

    private String actor() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "system";
    }
}
