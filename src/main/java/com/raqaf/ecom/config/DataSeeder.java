package com.raqaf.ecom.config;


import com.raqaf.ecom.entity.User;
import com.raqaf.ecom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * CONCEPT: CommandLineRunner - Spring Boot runs this automatically once,
 * right after the application context starts up.
 *
 * WHY: In a real system you'd never let clients self-register as ADMIN
 * (privilege escalation risk). Instead, the first admin account is either
 * seeded like this, or created manually via a secured internal tool.
 *
 * This checks if an admin already exists before creating one, so it's
 * safe to run on every startup (idempotent).
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String ADMIN_EMAIL = "admin@ecommerce.com";
    private static final String ADMIN_PASSWORD = "admin123"; // change this / move to env var for real use

    @Override
    public void run(String... args) {
        if (userRepository.existsByEmail(ADMIN_EMAIL)) {
            log.info("Admin user already exists, skipping seed.");
            return;
        }

        User admin = User.builder()
                .name("Admin")
                .email(ADMIN_EMAIL)
                .password(passwordEncoder.encode(ADMIN_PASSWORD))
                .role(User.Role.ADMIN)
                .build();

        userRepository.save(admin);
        log.info("Seeded default admin user -> email: {} | password: {}", ADMIN_EMAIL, ADMIN_PASSWORD);
    }
}
