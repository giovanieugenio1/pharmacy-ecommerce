package com.pharmacy.commerce.auth.infrastructure.config;

import com.pharmacy.commerce.auth.infrastructure.persistence.AdminUserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminDataInitializer implements ApplicationRunner {

    private final AdminUserJpaRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Value("${app.manager.email}")
    private String managerEmail;

    @Value("${app.manager.password}")
    private String managerPassword;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        adminUserRepository.findByEmail(adminEmail).ifPresent(admin -> {
            admin.setPassword(passwordEncoder.encode(adminPassword));
            adminUserRepository.save(admin);
            log.info("Senha do administrador '{}' atualizada via configuração de ambiente.", adminEmail);
        });

        adminUserRepository.findByEmail(managerEmail).ifPresent(manager -> {
            manager.setPassword(passwordEncoder.encode(managerPassword));
            adminUserRepository.save(manager);
            log.info("Senha do gerente '{}' atualizada via configuração de ambiente.", managerEmail);
        });
    }
}
