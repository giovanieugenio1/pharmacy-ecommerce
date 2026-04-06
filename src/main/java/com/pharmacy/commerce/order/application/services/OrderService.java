package com.pharmacy.commerce.order.application.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pharmacy.commerce.audit.application.services.AuditService;
import com.pharmacy.commerce.cart.infrastructure.persistence.CartJpaRepository;
import com.pharmacy.commerce.cart.infrastructure.persistence.entity.CartEntity;
import com.pharmacy.commerce.cart.infrastructure.persistence.entity.CartItemEntity;
import com.pharmacy.commerce.customer.infrastructure.persistence.AddressJpaRepository;
import com.pharmacy.commerce.customer.infrastructure.persistence.CustomerJpaRepository;
import com.pharmacy.commerce.customer.infrastructure.persistence.entity.AddressEntity;
import com.pharmacy.commerce.customer.infrastructure.persistence.entity.CustomerEntity;
import com.pharmacy.commerce.inventory.infrastructure.persistence.StoreInventoryJpaRepository;
import com.pharmacy.commerce.inventory.infrastructure.persistence.entity.StoreInventoryEntity;
import com.pharmacy.commerce.order.api.request.CheckoutRequest;
import com.pharmacy.commerce.order.api.request.UpdateOrderStatusRequest;
import com.pharmacy.commerce.order.api.response.OrderResponse;
import com.pharmacy.commerce.order.infrastructure.persistence.CustomerOrderJpaRepository;
import com.pharmacy.commerce.order.infrastructure.persistence.entity.CustomerOrderEntity;
import com.pharmacy.commerce.order.infrastructure.persistence.entity.OrderItemEntity;
import com.pharmacy.commerce.order.infrastructure.persistence.entity.OrderStatusHistoryEntity;
import com.pharmacy.commerce.pricing.infrastructure.persistence.ProductPriceJpaRepository;
import com.pharmacy.commerce.pricing.infrastructure.persistence.entity.ProductPriceEntity;
import com.pharmacy.commerce.shared.api.response.PageResponse;
import com.pharmacy.commerce.shared.exception.BusinessException;
import com.pharmacy.commerce.shared.exception.ResourceNotFoundException;
import com.pharmacy.commerce.store.infrastructure.persistence.StoreJpaRepository;
import com.pharmacy.commerce.store.infrastructure.persistence.entity.StoreEntity;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CustomerOrderJpaRepository orderRepository;
    private final CustomerJpaRepository customerRepository;
    private final CartJpaRepository cartRepository;
    private final AddressJpaRepository addressRepository;
    private final StoreJpaRepository storeRepository;
    private final ProductPriceJpaRepository priceRepository;
    private final StoreInventoryJpaRepository inventoryRepository;
    private final ObjectMapper objectMapper;
    private final AuditService auditService;

    private static final List<String> VALID_STATUSES = List.of(
            "CREATED", "CONFIRMED", "PREPARING", "READY", "OUT_FOR_DELIVERY", "DELIVERED", "CANCELLED"
    );

    @Transactional
    public OrderResponse checkout(String email, CheckoutRequest request) {
        CustomerEntity customer = findCustomer(email);

        CartEntity cart = cartRepository.findOpenCartByCustomerId(customer.getId())
                .orElseThrow(() -> new BusinessException("Carrinho vazio ou não encontrado"));

        if (cart.getItems().isEmpty()) {
            throw new BusinessException("O carrinho está vazio");
        }

        StoreEntity mainStore = storeRepository.findByIsMainTrue()
                .orElseThrow(() -> new BusinessException("Loja principal não configurada"));

        AddressEntity address = addressRepository.findByIdAndCustomerId(request.getAddressId(), customer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado"));

        validatePaymentMethod(request.getPaymentMethod());
        validateDeliveryMethod(request.getDeliveryMethod());

        List<Long> productIds = cart.getItems().stream().map(i -> i.getProduct().getId()).toList();

        Map<Long, ProductPriceEntity> priceMap = priceRepository
                .findMainStorePricesByProductIds(productIds).stream()
                .collect(Collectors.toMap(p -> p.getProduct().getId(), p -> p));

        Map<Long, StoreInventoryEntity> inventoryMap = inventoryRepository
                .findMainStoreInventoriesByProductIds(productIds).stream()
                .collect(Collectors.toMap(i -> i.getProduct().getId(), i -> i));

        List<OrderItemEntity> orderItems = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        for (CartItemEntity cartItem : cart.getItems()) {
            Long productId = cartItem.getProduct().getId();

            ProductPriceEntity price = priceMap.get(productId);
            if (price == null) {
                throw new BusinessException("Produto '" + cartItem.getProduct().getName() + "' sem preço cadastrado");
            }

            StoreInventoryEntity inventory = inventoryMap.get(productId);
            int available = inventory != null ? inventory.getPhysicalAvailable() : 0;
            if (available < cartItem.getQuantity()) {
                throw new BusinessException("Estoque insuficiente para o produto '" + cartItem.getProduct().getName() +
                        "'. Disponível: " + available);
            }

            BigDecimal unitPrice = price.getEffectivePrice();
            BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            subtotal = subtotal.add(totalPrice);

            orderItems.add(OrderItemEntity.builder()
                    .product(cartItem.getProduct())
                    .productNameSnapshot(cartItem.getProduct().getName())
                    .productSkuSnapshot(cartItem.getProduct().getSku())
                    .quantity(cartItem.getQuantity())
                    .unitPrice(unitPrice)
                    .totalPrice(totalPrice)
                    .build());

            if (inventory != null) {
                inventory.setAvailableQuantity(inventory.getAvailableQuantity() - cartItem.getQuantity());
                inventoryRepository.save(inventory);
            }
        }

        BigDecimal total = subtotal.add(BigDecimal.ZERO);

        CustomerOrderEntity order = CustomerOrderEntity.builder()
                .customer(customer)
                .store(mainStore)
                .orderNumber(generateOrderNumber())
                .subtotalAmount(subtotal)
                .totalAmount(total)
                .paymentMethod(request.getPaymentMethod().toUpperCase())
                .deliveryMethod(request.getDeliveryMethod().toUpperCase())
                .addressSnapshotJson(serializeAddress(address))
                .notes(request.getNotes())
                .estimatedDeliveryDate(estimateDeliveryDate(request.getDeliveryMethod()))
                .build();

        orderItems.forEach(item -> item.setOrder(order));
        order.getItems().addAll(orderItems);

        OrderStatusHistoryEntity history = OrderStatusHistoryEntity.builder()
                .order(order)
                .newStatus("CREATED")
                .notes("Pedido criado")
                .changedBy(email)
                .build();
        order.getStatusHistory().add(history);

        CustomerOrderEntity saved = orderRepository.save(order);

        cart.setStatus("CLOSED");
        cart.getItems().clear();
        cartRepository.save(cart);

        auditService.log(email, "CUSTOMER", customer.getId(), "CHECKOUT", "Order", saved.getId(), null, null, null, null);

        return OrderResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public PageResponse<OrderResponse> listMyOrders(String email, Pageable pageable) {
        CustomerEntity customer = findCustomer(email);
        return PageResponse.from(
                orderRepository.findAllByCustomerId(customer.getId(), pageable).map(OrderResponse::summary));
    }

    @Transactional(readOnly = true)
    public OrderResponse getMyOrder(String email, Long orderId) {
        CustomerEntity customer = findCustomer(email);
        return OrderResponse.from(orderRepository.findByIdAndCustomerId(orderId, customer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado")));
    }

    @Transactional
    public OrderResponse cancelMyOrder(String email, Long orderId) {
        CustomerEntity customer = findCustomer(email);
        CustomerOrderEntity order = orderRepository.findByIdAndCustomerId(orderId, customer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado"));

        if (!List.of("CREATED", "CONFIRMED").contains(order.getStatus())) {
            throw new BusinessException("Pedido não pode ser cancelado no status atual: " + order.getStatus());
        }

        return updateStatus(order, "CANCELLED", "Cancelado pelo cliente", email);
    }

    // Admin methods

    @Transactional(readOnly = true)
    public PageResponse<OrderResponse> listAllOrders(String status, Pageable pageable) {
        Specification<CustomerOrderEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (status != null && !status.isBlank())
                predicates.add(cb.equal(root.get("status"), status));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return PageResponse.from(orderRepository.findAll(spec, pageable).map(OrderResponse::summary));
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long orderId) {
        return OrderResponse.from(orderRepository.findByIdWithDetails(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado: " + orderId)));
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, UpdateOrderStatusRequest request, String changedBy) {
        CustomerOrderEntity order = orderRepository.findByIdWithDetails(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado: " + orderId));

        if (!VALID_STATUSES.contains(request.getStatus().toUpperCase())) {
            throw new BusinessException("Status inválido: " + request.getStatus() +
                    ". Valores aceitos: " + VALID_STATUSES);
        }

        return updateStatus(order, request.getStatus().toUpperCase(), request.getNotes(), changedBy);
    }

    private OrderResponse updateStatus(CustomerOrderEntity order, String newStatus, String notes, String changedBy) {
        String previousStatus = order.getStatus();
        order.setStatus(newStatus);

        if ("DELIVERED".equals(newStatus)) {
            order.setDeliveredAt(java.time.LocalDateTime.now());
        }

        OrderStatusHistoryEntity history = OrderStatusHistoryEntity.builder()
                .order(order)
                .previousStatus(previousStatus)
                .newStatus(newStatus)
                .notes(notes)
                .changedBy(changedBy)
                .build();
        order.getStatusHistory().add(history);

        CustomerOrderEntity saved = orderRepository.save(order);

        auditService.log(changedBy, "ADMIN", "UPDATE_STATUS", "Order", order.getId());

        return OrderResponse.from(saved);
    }

    private CustomerEntity findCustomer(String email) {
        return customerRepository.findActiveByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
    }

    private String generateOrderNumber() {
        return "PED-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String serializeAddress(AddressEntity address) {
        try {
            return objectMapper.writeValueAsString(Map.of(
                    "label", nullSafe(address.getLabel()),
                    "recipientName", nullSafe(address.getRecipientName()),
                    "street", nullSafe(address.getStreet()),
                    "number", nullSafe(address.getNumber()),
                    "complement", nullSafe(address.getComplement()),
                    "neighborhood", nullSafe(address.getNeighborhood()),
                    "city", nullSafe(address.getCity()),
                    "state", nullSafe(address.getState()),
                    "zipCode", nullSafe(address.getZipCode())
            ));
        } catch (JsonProcessingException e) {
            throw new BusinessException("Erro ao processar endereço");
        }
    }

    private String nullSafe(String value) {
        return value != null ? value : "";
    }

    private LocalDate estimateDeliveryDate(String deliveryMethod) {
        if ("PICKUP".equalsIgnoreCase(deliveryMethod)) return LocalDate.now().plusDays(1);
        return LocalDate.now().plusDays(3);
    }

    private void validatePaymentMethod(String method) {
        List<String> valid = List.of("CREDIT_CARD", "DEBIT_CARD", "PIX", "CASH", "BOLETO");
        if (!valid.contains(method.toUpperCase())) {
            throw new BusinessException("Método de pagamento inválido: " + method + ". Valores aceitos: " + valid);
        }
    }

    private void validateDeliveryMethod(String method) {
        List<String> valid = List.of("DELIVERY", "PICKUP");
        if (!valid.contains(method.toUpperCase())) {
            throw new BusinessException("Método de entrega inválido: " + method + ". Valores aceitos: " + valid);
        }
    }
}
