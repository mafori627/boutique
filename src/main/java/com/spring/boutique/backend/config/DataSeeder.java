package com.spring.boutique.backend.config;

import com.spring.boutique.backend.entity.Role;
import com.spring.boutique.backend.entity.User;
import com.spring.boutique.backend.repository.RoleRepository;
import com.spring.boutique.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Runs once on startup. Makes sure ROLE_CUSTOMER and ROLE_ADMIN exist,
 * so registration never fails because the roles table is empty.
 *
 * Also solves the "who promotes the first admin" chicken-and-egg problem:
 * set admin.bootstrap-email in application.yml to the email of a user who
 * has already registered normally, restart the app, and they'll be
 * promoted to ROLE_ADMIN automatically. From then on, that admin can use
 * POST /api/admin/promote for everyone else - no more restarts needed.
 */
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Value("${admin.bootstrap-email:}")
    private String bootstrapAdminEmail;

    @Override
    public void run(String... args) {
        if (roleRepository.findByName("ROLE_CUSTOMER").isEmpty()) {
            roleRepository.save(new Role("ROLE_CUSTOMER"));
        }
        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            roleRepository.save(new Role("ROLE_ADMIN"));
        }

        promoteBootstrapAdminIfConfigured();
    }

    private void promoteBootstrapAdminIfConfigured() {
        if (bootstrapAdminEmail == null || bootstrapAdminEmail.isBlank()) {
            return; // nothing configured, skip silently
        }

        Optional<User> maybeUser = userRepository.findByEmail(bootstrapAdminEmail);
        if (maybeUser.isEmpty()) {
            // user hasn't registered yet - nothing to promote, try again after they sign up + restart
            return;
        }

        User user = maybeUser.get();
        boolean alreadyAdmin = user.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_ADMIN"));
        if (alreadyAdmin) {
            return;
        }

        Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseThrow();
        user.getRoles().add(adminRole);
        userRepository.save(user);
    }
}
