package com.pharmacy.commerce.cart.application.services;

import com.pharmacy.commerce.cart.api.request.AddCartItemRequest;
import com.pharmacy.commerce.cart.api.request.UpdateCartItemRequest;
import com.pharmacy.commerce.cart.api.response.CartResponse;
import com.pharmacy.commerce.cart.infrastructure.persistence.CartItemJpaRepository;
import com.pharmacy.commerce.cart.infrastructure.persistence.CartJpaRepository;
import com.pharmacy.commerce.cart.infrastructure.persistence.entity.CartEntity;
import com.pharmacy.commerce.cart.infrastructure.persistence.entity.CartItemEntity;
import com.pharmacy.commerce.catalog.infrastructure.persistence.ProductJpaRepository;
import com.pharmacy.commerce.catalog.infrastructure.persistence.entity.ProductEntity;
import com.pharmacy.commerce.customer.infrastructure.persistence.CustomerJpaRepository;
import com.pharmacy.commerce.customer.infrastructure.persistence.entity.CustomerEntity;
import com.pharmacy.commerce.pricing.infrastructure.persistence.ProductPriceJpaRepository;
import com.pharmacy.commerce.pricing.infrastructure.persistence.entity.ProductPriceEntity;
import com.pharmacy.commerce.shared.exception.BusinessException;
import com.pharmacy.commerce.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartJpaRepository cartRepository;
    private final CartItemJpaRepository cartItemRepository;
    private final CustomerJpaRepository customerRepository;
    private final ProductJpaRepository productRepository;
    private final ProductPriceJpaRepository priceRepository;

    @Transactional(readOnly = true)
    public CartResponse getCart(String email) {
        CustomerEntity customer = findCustomer(email);
        CartEntity cart = cartRepository.findOpenCartByCustomerId(customer.getId())
                .orElseGet(() -> createEmptyCart(customer));
        return CartResponse.from(cart);
    }

    @Transactional
    public CartResponse addItem(String email, AddCartItemRequest request) {
        CustomerEntity customer = findCustomer(email);
        CartEntity cart = cartRepository.findOpenCartByCustomerId(customer.getId())
                .orElseGet(() -> cartRepository.save(CartEntity.builder().customer(customer).build()));

        ProductEntity product = productRepository.findById(request.getProductId())
                .filter(ProductEntity::getActive)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado ou inativo"));

        BigDecimal price = priceRepository.findMainStorePriceByProductId(product.getId())
                .map(ProductPriceEntity::getEffectivePrice)
                .orElseThrow(() -> new BusinessException("Produto sem preço cadastrado"));

        CartItemEntity existingItem = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), product.getId())
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
            existingItem.setUnitPriceSnapshot(price);
            cartItemRepository.save(existingItem);
        } else {
            CartItemEntity newItem = CartItemEntity.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.getQuantity())
                    .unitPriceSnapshot(price)
                    .build();
            cart.getItems().add(newItem);
        }

        return CartResponse.from(cartRepository.save(cart));
    }

    @Transactional
    public CartResponse updateItem(String email, Long itemId, UpdateCartItemRequest request) {
        CustomerEntity customer = findCustomer(email);
        CartEntity cart = findOpenCart(customer);

        CartItemEntity item = cart.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado no carrinho"));

        if (request.getQuantity() == 0) {
            cart.getItems().remove(item);
        } else {
            item.setQuantity(request.getQuantity());
        }

        return CartResponse.from(cartRepository.save(cart));
    }

    @Transactional
    public CartResponse removeItem(String email, Long itemId) {
        CustomerEntity customer = findCustomer(email);
        CartEntity cart = findOpenCart(customer);

        boolean removed = cart.getItems().removeIf(i -> i.getId().equals(itemId));
        if (!removed) {
            throw new ResourceNotFoundException("Item não encontrado no carrinho");
        }

        return CartResponse.from(cartRepository.save(cart));
    }

    @Transactional
    public void clearCart(String email) {
        CustomerEntity customer = findCustomer(email);
        cartRepository.findOpenCartByCustomerId(customer.getId()).ifPresent(cart -> {
            cart.getItems().clear();
            cartRepository.save(cart);
        });
    }

    private CustomerEntity findCustomer(String email) {
        return customerRepository.findActiveByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
    }

    private CartEntity findOpenCart(CustomerEntity customer) {
        return cartRepository.findOpenCartByCustomerId(customer.getId())
                .orElseThrow(() -> new BusinessException("Carrinho não encontrado"));
    }

    private CartEntity createEmptyCart(CustomerEntity customer) {
        return CartEntity.builder().customer(customer).build();
    }
}
