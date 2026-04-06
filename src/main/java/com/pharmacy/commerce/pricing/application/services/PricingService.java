package com.pharmacy.commerce.pricing.application.services;

import com.pharmacy.commerce.audit.application.services.AuditService;
import com.pharmacy.commerce.catalog.infrastructure.persistence.ProductJpaRepository;
import com.pharmacy.commerce.catalog.infrastructure.persistence.entity.ProductEntity;
import com.pharmacy.commerce.pricing.api.request.SetPriceRequest;
import com.pharmacy.commerce.pricing.api.response.PriceResponse;
import com.pharmacy.commerce.pricing.infrastructure.persistence.ProductPriceJpaRepository;
import com.pharmacy.commerce.pricing.infrastructure.persistence.entity.ProductPriceEntity;
import com.pharmacy.commerce.shared.exception.BusinessException;
import com.pharmacy.commerce.shared.exception.ResourceNotFoundException;
import com.pharmacy.commerce.store.infrastructure.persistence.StoreJpaRepository;
import com.pharmacy.commerce.store.infrastructure.persistence.entity.StoreEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PricingService {

    private final ProductPriceJpaRepository priceRepository;
    private final ProductJpaRepository productRepository;
    private final StoreJpaRepository storeRepository;
    private final AuditService auditService;

    @Transactional
    public PriceResponse setPrice(SetPriceRequest request) {
        validatePromotion(request);

        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado: " + request.getProductId()));

        StoreEntity store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new ResourceNotFoundException("Loja não encontrada: " + request.getStoreId()));

        boolean isNew = !priceRepository.existsByProductIdAndStoreId(request.getProductId(), request.getStoreId());

        ProductPriceEntity entity = priceRepository
                .findByProductIdAndStoreId(request.getProductId(), request.getStoreId())
                .orElse(ProductPriceEntity.builder().product(product).store(store).build());

        entity.setPrice(request.getPrice());
        entity.setPromotionalPrice(request.getPromotionalPrice());
        entity.setPromotionStartAt(request.getPromotionStartAt());
        entity.setPromotionEndAt(request.getPromotionEndAt());
        entity.setActive(true);

        ProductPriceEntity saved = priceRepository.save(entity);
        auditService.log(actor(), "ADMIN", isNew ? "CREATE" : "UPDATE", "ProductPrice", saved.getId());
        return PriceResponse.from(saved);
    }

    @Transactional
    public PriceResponse removePromotion(Long priceId) {
        ProductPriceEntity entity = findEntityById(priceId);
        entity.setPromotionalPrice(null);
        entity.setPromotionStartAt(null);
        entity.setPromotionEndAt(null);
        ProductPriceEntity saved = priceRepository.save(entity);
        auditService.log(actor(), "ADMIN", "REMOVE_PROMOTION", "ProductPrice", priceId);
        return PriceResponse.from(saved);
    }

    @Transactional
    public void deactivate(Long priceId) {
        ProductPriceEntity entity = findEntityById(priceId);
        entity.setActive(false);
        priceRepository.save(entity);
        auditService.log(actor(), "ADMIN", "DELETE", "ProductPrice", priceId);
    }

    @Transactional(readOnly = true)
    public List<PriceResponse> findByProductId(Long productId) {
        return priceRepository.findAllByProductId(productId).stream().map(PriceResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public PriceResponse findById(Long id) {
        return PriceResponse.from(findEntityById(id));
    }

    private void validatePromotion(SetPriceRequest request) {
        if (request.getPromotionalPrice() != null) {
            if (request.getPromotionalPrice().compareTo(request.getPrice()) >= 0) {
                throw new BusinessException("Preço promocional deve ser menor que o preço regular");
            }
            if (request.getPromotionStartAt() != null && request.getPromotionEndAt() != null
                    && !request.getPromotionEndAt().isAfter(request.getPromotionStartAt())) {
                throw new BusinessException("Data de fim da promoção deve ser posterior à data de início");
            }
        }
    }

    private ProductPriceEntity findEntityById(Long id) {
        return priceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Preço não encontrado: " + id));
    }

    private String actor() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "system";
    }
}
