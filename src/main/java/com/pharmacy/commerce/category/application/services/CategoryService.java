package com.pharmacy.commerce.category.application.services;

import com.pharmacy.commerce.audit.application.services.AuditService;
import com.pharmacy.commerce.category.api.request.CreateCategoryRequest;
import com.pharmacy.commerce.category.api.request.UpdateCategoryRequest;
import com.pharmacy.commerce.category.api.response.CategoryResponse;
import com.pharmacy.commerce.category.infrastructure.persistence.CategoryJpaRepository;
import com.pharmacy.commerce.category.infrastructure.persistence.entity.CategoryEntity;
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
public class CategoryService {

    private final CategoryJpaRepository repository;
    private final AuditService auditService;

    @Transactional
    public CategoryResponse create(CreateCategoryRequest request) {
        String slug = SlugUtils.toSlug(request.getName());

        if (repository.existsBySlug(slug)) {
            throw new BusinessException("Já existe uma categoria com o nome '" + request.getName() + "'");
        }

        CategoryEntity parent = null;
        if (request.getParentId() != null) {
            parent = repository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria pai não encontrada"));
        }

        CategoryEntity entity = CategoryEntity.builder()
                .name(request.getName())
                .slug(slug)
                .parent(parent)
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                .active(true)
                .build();

        CategoryEntity saved = repository.save(entity);
        auditService.log(actor(), "ADMIN", "CREATE", "Category", saved.getId());
        return CategoryResponse.from(saved);
    }

    @Transactional
    public CategoryResponse update(Long id, UpdateCategoryRequest request) {
        CategoryEntity entity = findEntityById(id);

        if (request.getName() != null && !request.getName().equals(entity.getName())) {
            String newSlug = SlugUtils.toSlug(request.getName());
            if (repository.existsBySlugAndIdNot(newSlug, id)) {
                throw new BusinessException("Já existe uma categoria com o nome '" + request.getName() + "'");
            }
            entity.setName(request.getName());
            entity.setSlug(newSlug);
        }

        if (request.getParentId() != null) {
            if (request.getParentId().equals(id)) {
                throw new BusinessException("Uma categoria não pode ser pai de si mesma");
            }
            CategoryEntity parent = repository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria pai não encontrada"));
            entity.setParent(parent);
        }

        if (request.getDescription() != null) entity.setDescription(request.getDescription());
        if (request.getImageUrl() != null) entity.setImageUrl(request.getImageUrl());
        if (request.getDisplayOrder() != null) entity.setDisplayOrder(request.getDisplayOrder());
        if (request.getActive() != null) entity.setActive(request.getActive());

        CategoryEntity saved = repository.save(entity);
        auditService.log(actor(), "ADMIN", "UPDATE", "Category", id);
        return CategoryResponse.from(saved);
    }

    @Transactional
    public void delete(Long id) {
        CategoryEntity entity = findEntityById(id);
        entity.setActive(false);
        repository.save(entity);
        auditService.log(actor(), "ADMIN", "DELETE", "Category", id);
    }

    @Transactional(readOnly = true)
    public PageResponse<CategoryResponse> findAll(Pageable pageable) {
        return PageResponse.from(repository.findAll(pageable).map(CategoryResponse::from));
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> findAllActive() {
        return repository.findAllActiveWithParent().stream().map(CategoryResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponse findBySlug(String slug) {
        return CategoryResponse.from(repository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada: " + slug)));
    }

    @Transactional(readOnly = true)
    public CategoryResponse findById(Long id) {
        return CategoryResponse.from(findEntityById(id));
    }

    private CategoryEntity findEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada: " + id));
    }

    private String actor() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "system";
    }
}
