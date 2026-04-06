package com.pharmacy.commerce.auth.application.services;

import com.pharmacy.commerce.auth.infrastructure.persistence.AdminUserJpaRepository;
import com.pharmacy.commerce.auth.infrastructure.persistence.entity.AdminUserEntity;
import com.pharmacy.commerce.customer.infrastructure.persistence.CustomerJpaRepository;
import com.pharmacy.commerce.customer.infrastructure.persistence.entity.CustomerEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminUserJpaRepository adminUserRepository;
    private final CustomerJpaRepository customerRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<AdminUserEntity> admin = adminUserRepository.findActiveByEmailWithRole(email);
        if (admin.isPresent()) {
            AdminUserEntity user = admin.get();
            List<GrantedAuthority> authorities = Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_" + user.getRole().getName()));
            return User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .authorities(authorities)
                    .accountLocked(!user.getActive())
                    .disabled(!user.getActive())
                    .build();
        }

        CustomerEntity customer = customerRepository.findActiveByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_CUSTOMER"));
        return User.builder()
                .username(customer.getEmail())
                .password(customer.getPassword())
                .authorities(authorities)
                .accountLocked(!customer.getActive())
                .disabled(!customer.getActive())
                .build();
    }
}
