package com.example.bookexchange.config;

import com.example.bookexchange.entity.Book;
import com.example.bookexchange.entity.BookStatus;
import com.example.bookexchange.entity.Role;
import com.example.bookexchange.entity.RoleName;
import com.example.bookexchange.entity.User;
import com.example.bookexchange.repository.BookRepository;
import com.example.bookexchange.repository.RoleRepository;
import com.example.bookexchange.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Set;

@Configuration
public class DataSeeder {
    @Bean
    CommandLineRunner seedData(RoleRepository roleRepository,
                               UserRepository userRepository,
                               BookRepository bookRepository,
                               PasswordEncoder passwordEncoder) {
        return args -> {
            for (RoleName roleName : RoleName.values()) {
                roleRepository.findByName(roleName).orElseGet(() -> roleRepository.save(new Role(roleName)));
            }

            if (userRepository.findByEmail("admin@bookexchange.com").isEmpty()) {
                Role adminRole = roleRepository.findByName(RoleName.ADMIN).orElseThrow();
                User admin = User.builder()
                    .fullName("System Admin")
                    .email("admin@bookexchange.com")
                    .password(passwordEncoder.encode("Admin@123"))
                    .enabled(true)
                    .roles(Set.of(adminRole))
                    .build();
                userRepository.save(admin);
            }

            if (userRepository.findByEmail("user@bookexchange.com").isEmpty()) {
                Role userRole = roleRepository.findByName(RoleName.USER).orElseThrow();
                User user = User.builder()
                    .fullName("Normal User")
                    .email("user@bookexchange.com")
                    .password(passwordEncoder.encode("User@123"))
                    .enabled(true)
                    .roles(Set.of(userRole))
                    .build();
                userRepository.save(user);
            }

            if (bookRepository.count() == 0) {
                User admin = userRepository.findByEmail("admin@bookexchange.com").orElseThrow();
                bookRepository.save(Book.builder().title("Clean Code").author("Robert C. Martin")
                    .price(BigDecimal.valueOf(450)).status(BookStatus.AVAILABLE).seller(admin).build());
                bookRepository.save(Book.builder().title("Design Patterns").author("GoF")
                    .price(BigDecimal.valueOf(550)).status(BookStatus.AVAILABLE).seller(admin).build());
            }
        };
    }
}
