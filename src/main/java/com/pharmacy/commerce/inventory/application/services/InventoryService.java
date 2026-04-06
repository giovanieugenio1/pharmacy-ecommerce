package com.pharmacy.commerce.inventory.application.services;

import com.pharmacy.commerce.audit.application.services.AuditService;
import com.pharmacy.commerce.catalog.infrastructure.persistence.ProductJpaRepository;
import com.pharmacy.commerce.catalog.infrastructure.persistence.entity.ProductEntity;
import com.pharmacy.commerce.inventory.api.request.AdjustInventoryRequest;
import com.pharmacy.commerce.inventory.api.request.SetInventoryRequest;
import com.pharmacy.commerce.inventory.api.response.InventoryResponse;
import com.pharmacy.commerce.inventory.infrastructure.persistence.StoreInventoryJpaRepository;
import com.pharmacy.commerce.inventory.infrastructure.persistence.entity.StoreInventoryEntity;
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
public class InventoryService {

    private final StoreInventoryJpaRepository inventoryRepository;
    private final ProductJpaRepository productRepository;
    private final StoreJpaRepository storeRepository;
    private final AuditService auditService;

    @Transactional
    public InventoryResponse setInventory(SetInventoryRequest request) {
        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado: " + request.getProductId()));

        StoreEntity store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new ResourceNotFoundException("Loja não encontrada: " + request.getStoreId()));

        boolean isNew = inventoryRepository
                .findByProductIdAndStoreId(request.getProductId(), request.getStoreId()).isEmpty();

        StoreInventoryEntity entity = inventoryRepository
                .findByProductIdAndStoreId(request.getProductId(), request.getStoreId())
                .orElse(StoreInventoryEntity.builder().product(product).store(store).build());

        entity.setAvailableQuantity(request.getAvailableQuantity());
        entity.setMinimumQuantity(request.getMinimumQuantity() != null ? request.getMinimumQuantity() : 0);
        entity.setActive(true);

        StoreInventoryEntity saved = inventoryRepository.save(entity);
        auditService.log(actor(), "ADMIN", isNew ? "CREATE" : "UPDATE", "StoreInventory", saved.getId());
        return InventoryResponse.from(saved);
    }

    @Transactional
    public InventoryResponse adjust(Long inventoryId, AdjustInventoryRequest request) {
        StoreInventoryEntity entity = findEntityById(inventoryId);

        int newQty = entity.getAvailableQuantity() + request.getQuantity();
        if (newQty < 0) {
            throw new BusinessException(
                    "Ajuste resultaria em estoque negativo. Estoque atual: " + entity.getAvailableQuantity());
        }

        entity.setAvailableQuantity(newQty);
        StoreInventoryEntity saved = inventoryRepository.save(entity);
        String action = request.getQuantity() >= 0 ? "INVENTORY_IN" : "INVENTORY_OUT";
        auditService.log(actor(), "ADMIN", action, "StoreInventory", inventoryId);
        return InventoryResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<InventoryResponse> findByProductId(Long productId) {
        return inventoryRepository.findAllByProductId(productId).stream().map(InventoryResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public InventoryResponse findById(Long id) {
        return InventoryResponse.from(findEntityById(id));
    }

    private StoreInventoryEntity findEntityById(Long id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado: " + id));
    }

    private String actor() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "system";
    }
}
