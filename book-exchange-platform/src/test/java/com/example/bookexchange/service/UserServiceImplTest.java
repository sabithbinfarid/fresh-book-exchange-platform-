package com.example.bookexchange.service;

import com.example.bookexchange.dto.RegisterRequest;
import com.example.bookexchange.entity.Role;
import com.example.bookexchange.entity.RoleName;
import com.example.bookexchange.entity.User;
import com.example.bookexchange.exception.BadRequestException;
import com.example.bookexchange.exception.ResourceNotFoundException;
import com.example.bookexchange.repository.RoleRepository;
import com.example.bookexchange.repository.UserRepository;
import com.example.bookexchange.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock UserRepository userRepository;
    @Mock RoleRepository roleRepository;
    @Mock PasswordEncoder passwordEncoder;
    @InjectMocks UserServiceImpl userService;

    RegisterRequest request;
    Role userRole;

    @BeforeEach
    void setUp() {
        request = new RegisterRequest();
        request.setFullName("User One");
        request.setEmail("user@example.com");
        request.setPassword("secret123");
        userRole = new Role(RoleName.USER);
    }

    @Test void register_shouldCreateUser() {
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(roleRepository.findByName(RoleName.USER)).thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode(request.getPassword())).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenAnswer(i -> { User u = i.getArgument(0); u.setId(1L); return u;});

        var response = userService.register(request);

        assertEquals("user@example.com", response.getEmail());
        assertTrue(response.getRoles().contains("USER"));
        verify(userRepository).save(any(User.class));
    }

    @Test void register_shouldRejectDuplicateEmail() {
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);
        assertThrows(BadRequestException.class, () -> userService.register(request));
    }

    @Test void register_shouldThrowWhenRoleMissing() {
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(roleRepository.findByName(RoleName.USER)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.register(request));
    }

    @Test void findAll_shouldReturnUsers() {
        User user = User.builder().id(1L).fullName("A").email("a@a.com").password("p").roles(Set.of(userRole)).build();
        when(userRepository.findAll()).thenReturn(List.of(user));
        assertEquals(1, userService.findAll().size());
    }

    @Test void findById_shouldReturnUser() {
        User user = User.builder().id(1L).fullName("A").email("a@a.com").password("p").roles(Set.of(userRole)).build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        assertEquals(1L, userService.findById(1L).getId());
    }

    @Test void findById_shouldThrowWhenMissing() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.findById(99L));
    }
}
