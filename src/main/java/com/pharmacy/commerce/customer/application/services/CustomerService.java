package com.pharmacy.commerce.customer.application.services;

import com.pharmacy.commerce.audit.application.services.AuditService;
import com.pharmacy.commerce.customer.api.request.CreateAddressRequest;
import com.pharmacy.commerce.customer.api.request.RegisterCustomerRequest;
import com.pharmacy.commerce.customer.api.request.UpdateCustomerRequest;
import com.pharmacy.commerce.customer.api.response.AddressResponse;
import com.pharmacy.commerce.customer.api.response.CustomerResponse;
import com.pharmacy.commerce.customer.infrastructure.persistence.AddressJpaRepository;
import com.pharmacy.commerce.customer.infrastructure.persistence.CustomerJpaRepository;
import com.pharmacy.commerce.customer.infrastructure.persistence.entity.AddressEntity;
import com.pharmacy.commerce.customer.infrastructure.persistence.entity.CustomerEntity;
import com.pharmacy.commerce.shared.api.response.PageResponse;
import com.pharmacy.commerce.shared.exception.BusinessException;
import com.pharmacy.commerce.shared.exception.ResourceNotFoundException;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerJpaRepository customerRepository;
    private final AddressJpaRepository addressRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditService auditService;

    @Transactional
    public CustomerResponse register(RegisterCustomerRequest request) {
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("E-mail já cadastrado");
        }
        if (request.getCpf() != null && customerRepository.existsByCpf(request.getCpf())) {
            throw new BusinessException("CPF já cadastrado");
        }

        CustomerEntity entity = CustomerEntity.builder()
                .name(request.getName())
                .email(request.getEmail().toLowerCase())
                .password(passwordEncoder.encode(request.getPassword()))
                .cpf(request.getCpf())
                .phone(request.getPhone())
                .birthDate(request.getBirthDate())
                .active(true)
                .build();

        CustomerEntity saved = customerRepository.save(entity);
        auditService.log(saved.getEmail(), "CUSTOMER", saved.getId(), "REGISTER", "Customer", saved.getId(), null, null, null, null);
        return CustomerResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public CustomerResponse getProfile(String email) {
        return CustomerResponse.fromWithAddresses(findActiveByEmail(email));
    }

    @Transactional
    public CustomerResponse updateProfile(String email, UpdateCustomerRequest request) {
        CustomerEntity entity = findActiveByEmail(email);
        if (request.getName() != null) entity.setName(request.getName());
        if (request.getPhone() != null) entity.setPhone(request.getPhone());
        if (request.getBirthDate() != null) entity.setBirthDate(request.getBirthDate());
        CustomerEntity saved = customerRepository.save(entity);
        auditService.log(email, "CUSTOMER", saved.getId(), "UPDATE_PROFILE", "Customer", saved.getId(), null, null, null, null);
        return CustomerResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<AddressResponse> listAddresses(String email) {
        CustomerEntity customer = findActiveByEmail(email);
        return addressRepository.findAllByCustomerIdAndActiveTrueOrderByIsDefaultDescCreatedAtDesc(customer.getId())
                .stream().map(AddressResponse::from).toList();
    }

    @Transactional
    public AddressResponse addAddress(String email, CreateAddressRequest request) {
        CustomerEntity customer = findActiveByEmail(email);

        if (Boolean.TRUE.equals(request.getIsDefault())) {
            addressRepository.clearDefaultExcept(customer.getId(), -1L);
        }

        AddressEntity address = AddressEntity.builder()
                .customer(customer)
                .label(request.getLabel())
                .recipientName(request.getRecipientName())
                .street(request.getStreet())
                .number(request.getNumber())
                .complement(request.getComplement())
                .neighborhood(request.getNeighborhood())
                .city(request.getCity())
                .state(request.getState().toUpperCase())
                .zipCode(request.getZipCode())
                .isDefault(Boolean.TRUE.equals(request.getIsDefault()))
                .active(true)
                .build();

        AddressEntity saved = addressRepository.save(address);
        auditService.log(email, "CUSTOMER", customer.getId(), "ADD_ADDRESS", "Address", saved.getId(), null, null, null, null);
        return AddressResponse.from(saved);
    }

    @Transactional
    public AddressResponse updateAddress(String email, Long addressId, CreateAddressRequest request) {
        CustomerEntity customer = findActiveByEmail(email);
        AddressEntity address = addressRepository.findByIdAndCustomerId(addressId, customer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado"));

        if (Boolean.TRUE.equals(request.getIsDefault())) {
            addressRepository.clearDefaultExcept(customer.getId(), addressId);
        }

        address.setLabel(request.getLabel());
        address.setRecipientName(request.getRecipientName());
        address.setStreet(request.getStreet());
        address.setNumber(request.getNumber());
        address.setComplement(request.getComplement());
        address.setNeighborhood(request.getNeighborhood());
        address.setCity(request.getCity());
        address.setState(request.getState().toUpperCase());
        address.setZipCode(request.getZipCode());
        address.setIsDefault(Boolean.TRUE.equals(request.getIsDefault()));

        AddressEntity saved = addressRepository.save(address);
        auditService.log(email, "CUSTOMER", customer.getId(), "UPDATE_ADDRESS", "Address", addressId, null, null, null, null);
        return AddressResponse.from(saved);
    }

    @Transactional
    public void removeAddress(String email, Long addressId) {
        CustomerEntity customer = findActiveByEmail(email);
        AddressEntity address = addressRepository.findByIdAndCustomerId(addressId, customer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado"));
        address.setActive(false);
        addressRepository.save(address);
        auditService.log(email, "CUSTOMER", customer.getId(), "DELETE_ADDRESS", "Address", addressId, null, null, null, null);
    }

    @Transactional
    public AddressResponse setDefaultAddress(String email, Long addressId) {
        CustomerEntity customer = findActiveByEmail(email);
        AddressEntity address = addressRepository.findByIdAndCustomerId(addressId, customer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado"));
        addressRepository.clearDefaultExcept(customer.getId(), addressId);
        address.setIsDefault(true);
        return AddressResponse.from(addressRepository.save(address));
    }

    @Transactional(readOnly = true)
    public PageResponse<CustomerResponse> findAll(String search, Pageable pageable) {
        Specification<CustomerEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (search != null && !search.isBlank()) {
                String like = "%" + search.toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("name")), like),
                        cb.like(cb.lower(root.get("email")), like)
                ));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return PageResponse.from(customerRepository.findAll(spec, pageable).map(CustomerResponse::from));
    }

    @Transactional(readOnly = true)
    public CustomerResponse findById(Long id) {
        return CustomerResponse.fromWithAddresses(customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado: " + id)));
    }

    private CustomerEntity findActiveByEmail(String email) {
        return customerRepository.findActiveByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
    }
}
