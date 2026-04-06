package com.pharmacy.commerce.catalog.application.services;

import com.pharmacy.commerce.audit.application.services.AuditService;
import com.pharmacy.commerce.brand.infrastructure.persistence.BrandJpaRepository;
import com.pharmacy.commerce.brand.infrastructure.persistence.entity.BrandEntity;
import com.pharmacy.commerce.catalog.api.request.CreateProductRequest;
import com.pharmacy.commerce.catalog.api.request.ProductImageRequest;
import com.pharmacy.commerce.catalog.api.request.UpdateProductRequest;
import com.pharmacy.commerce.catalog.api.response.ProductDetailResponse;
import com.pharmacy.commerce.catalog.api.response.ProductSummaryResponse;
import com.pharmacy.commerce.catalog.infrastructure.persistence.ProductJpaRepository;
import com.pharmacy.commerce.catalog.infrastructure.persistence.entity.ProductEntity;
import com.pharmacy.commerce.catalog.infrastructure.persistence.entity.ProductImageEntity;
import com.pharmacy.commerce.category.infrastructure.persistence.CategoryJpaRepository;
import com.pharmacy.commerce.category.infrastructure.persistence.entity.CategoryEntity;
import com.pharmacy.commerce.inventory.infrastructure.persistence.StoreInventoryJpaRepository;
import com.pharmacy.commerce.inventory.infrastructure.persistence.entity.StoreInventoryEntity;
import com.pharmacy.commerce.pricing.infrastructure.persistence.ProductPriceJpaRepository;
import com.pharmacy.commerce.pricing.infrastructure.persistence.entity.ProductPriceEntity;
import com.pharmacy.commerce.shared.api.response.PageResponse;
import com.pharmacy.commerce.shared.exception.BusinessException;
import com.pharmacy.commerce.shared.exception.ResourceNotFoundException;
import com.pharmacy.commerce.shared.util.SlugUtils;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductJpaRepository productRepository;
    private final CategoryJpaRepository categoryRepository;
    private final BrandJpaRepository brandRepository;
    private final ProductPriceJpaRepository priceRepository;
    private final StoreInventoryJpaRepository inventoryRepository;
    private final AuditService auditService;

    @Transactional
    public ProductDetailResponse create(CreateProductRequest request) {
        String slug = SlugUtils.toSlug(request.getName());

        if (productRepository.existsBySlug(slug)) {
            throw new BusinessException("Já existe um produto com o nome '" + request.getName() + "'");
        }
        if (request.getSku() != null && productRepository.existsBySku(request.getSku())) {
            throw new BusinessException("Já existe um produto com o SKU '" + request.getSku() + "'");
        }

        ProductEntity entity = ProductEntity.builder()
                .name(request.getName())
                .slug(slug)
                .sku(request.getSku())
                .category(resolveCategory(request.getCategoryId()))
                .brand(resolveBrand(request.getBrandId()))
                .shortDescription(request.getShortDescription())
                .fullDescription(request.getFullDescription())
                .featured(request.getFeatured() != null ? request.getFeatured() : false)
                .requiresPrescription(request.getRequiresPrescription() != null ? request.getRequiresPrescription() : false)
                .controlledItem(request.getControlledItem() != null ? request.getControlledItem() : false)
                .active(true)
                .build();

        if (request.getImages() != null) {
            request.getImages().forEach(img -> entity.getImages().add(toImageEntity(img)));
        }

        ProductEntity saved = productRepository.save(entity);
        auditService.log(actor(), "ADMIN", "CREATE", "Product", saved.getId());
        return ProductDetailResponse.from(saved);
    }

    @Transactional
    public ProductDetailResponse update(Long id, UpdateProductRequest request) {
        ProductEntity entity = findEntityById(id);

        if (request.getName() != null && !request.getName().equals(entity.getName())) {
            String newSlug = SlugUtils.toSlug(request.getName());
            if (productRepository.existsBySlugAndIdNot(newSlug, id)) {
                throw new BusinessException("Já existe um produto com o nome '" + request.getName() + "'");
            }
            entity.setName(request.getName());
            entity.setSlug(newSlug);
        }
        if (request.getSku() != null && !request.getSku().equals(entity.getSku())) {
            if (productRepository.existsBySkuAndIdNot(request.getSku(), id)) {
                throw new BusinessException("Já existe um produto com o SKU '" + request.getSku() + "'");
            }
            entity.setSku(request.getSku());
        }
        if (request.getCategoryId() != null) entity.setCategory(resolveCategory(request.getCategoryId()));
        if (request.getBrandId() != null) entity.setBrand(resolveBrand(request.getBrandId()));
        if (request.getShortDescription() != null) entity.setShortDescription(request.getShortDescription());
        if (request.getFullDescription() != null) entity.setFullDescription(request.getFullDescription());
        if (request.getFeatured() != null) entity.setFeatured(request.getFeatured());
        if (request.getRequiresPrescription() != null) entity.setRequiresPrescription(request.getRequiresPrescription());
        if (request.getControlledItem() != null) entity.setControlledItem(request.getControlledItem());
        if (request.getActive() != null) entity.setActive(request.getActive());
        if (request.getImages() != null) {
            entity.getImages().clear();
            request.getImages().forEach(img -> entity.getImages().add(toImageEntity(img)));
        }

        ProductEntity saved = productRepository.save(entity);
        auditService.log(actor(), "ADMIN", "UPDATE", "Product", id);
        return ProductDetailResponse.from(saved);
    }

    @Transactional
    public ProductDetailResponse toggleActive(Long id) {
        ProductEntity entity = findEntityById(id);
        entity.setActive(!entity.getActive());
        ProductEntity saved = productRepository.save(entity);
        auditService.log(actor(), "ADMIN", saved.getActive() ? "ACTIVATE" : "DEACTIVATE", "Product", id);
        return ProductDetailResponse.from(saved);
    }

    @Transactional
    public void delete(Long id) {
        ProductEntity entity = findEntityById(id);
        entity.setActive(false);
        productRepository.save(entity);
        auditService.log(actor(), "ADMIN", "DELETE", "Product", id);
    }

    @Transactional(readOnly = true)
    public PageResponse<ProductSummaryResponse> findAll(
            Boolean active, Boolean featured, Long categoryId, Long brandId, String search, Pageable pageable) {

        Specification<ProductEntity> spec = buildSpec(active, featured, categoryId, brandId, search);
        Page<ProductEntity> page = productRepository.findAll(spec, pageable);
        List<Long> ids = page.map(ProductEntity::getId).toList();

        Map<Long, ProductPriceEntity> priceMap = ids.isEmpty() ? Map.of() :
                priceRepository.findMainStorePricesByProductIds(ids)
                        .stream().collect(Collectors.toMap(p -> p.getProduct().getId(), p -> p));

        Map<Long, StoreInventoryEntity> inventoryMap = ids.isEmpty() ? Map.of() :
                inventoryRepository.findMainStoreInventoriesByProductIds(ids)
                        .stream().collect(Collectors.toMap(i -> i.getProduct().getId(), i -> i));

        return PageResponse.from(page.map(product -> ProductSummaryResponse.from(
                product,
                priceMap.get(product.getId()),
                inventoryMap.containsKey(product.getId()) ? inventoryMap.get(product.getId()).isInStock() : null,
                inventoryMap.containsKey(product.getId()) ? inventoryMap.get(product.getId()).getPhysicalAvailable() : null
        )));
    }

    @Transactional(readOnly = true)
    public ProductDetailResponse findBySlug(String slug) {
        ProductEntity entity = productRepository.findBySlugWithDetails(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado: " + slug));
        ProductPriceEntity price = priceRepository.findMainStorePriceByProductId(entity.getId()).orElse(null);
        StoreInventoryEntity inventory = inventoryRepository.findMainStoreInventoryByProductId(entity.getId()).orElse(null);
        return ProductDetailResponse.from(entity, price, inventory);
    }

    @Transactional(readOnly = true)
    public ProductDetailResponse findById(Long id) {
        ProductEntity entity = findEntityById(id);
        ProductPriceEntity price = priceRepository.findMainStorePriceByProductId(id).orElse(null);
        StoreInventoryEntity inventory = inventoryRepository.findMainStoreInventoryByProductId(id).orElse(null);
        return ProductDetailResponse.from(entity, price, inventory);
    }

    private ProductEntity findEntityById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado: " + id));
    }

    private CategoryEntity resolveCategory(Long categoryId) {
        if (categoryId == null) return null;
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada: " + categoryId));
    }

    private BrandEntity resolveBrand(Long brandId) {
        if (brandId == null) return null;
        return brandRepository.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("Marca não encontrada: " + brandId));
    }

    private ProductImageEntity toImageEntity(ProductImageRequest img) {
        return ProductImageEntity.builder()
                .imageUrl(img.getImageUrl())
                .altText(img.getAltText())
                .displayOrder(img.getDisplayOrder() != null ? img.getDisplayOrder() : 0)
                .isPrimary(img.getIsPrimary() != null ? img.getIsPrimary() : false)
                .build();
    }

    private Specification<ProductEntity> buildSpec(Boolean active, Boolean featured,
                                                     Long categoryId, Long brandId, String search) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (active != null)
                predicates.add(cb.equal(root.get("active"), active));
            if (featured != null)
                predicates.add(cb.equal(root.get("featured"), featured));
            if (categoryId != null)
                predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            if (brandId != null)
                predicates.add(cb.equal(root.get("brand").get("id"), brandId));
            if (search != null && !search.isBlank())
                predicates.add(cb.like(cb.lower(root.get("name")),
                        "%" + search.toLowerCase() + "%"));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private String actor() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "system";
    }
}
