package com.example.bookexchange.controller;

import com.example.bookexchange.exception.ResourceNotFoundException;
import com.example.bookexchange.repository.UserRepository;
import com.example.bookexchange.service.OrderService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    private final UserRepository userRepository;
    private final OrderService orderService;

    public AuthRestController(UserRepository userRepository, OrderService orderService) {
        this.userRepository = userRepository;
        this.orderService = orderService;
    }

    @GetMapping("/me")
    public Map<String, Object> me(Authentication authentication) {
        Long userId = userRepository.findByEmail(authentication.getName())
            .map(user -> user.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));

        List<String> roles = authentication.getAuthorities().stream()
            .map(grantedAuthority -> grantedAuthority.getAuthority())
            .toList();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("userId", userId);
        response.put("username", authentication.getName());
        response.put("roles", roles);
        response.put("isAdmin", roles.contains("ROLE_ADMIN"));
        response.put("borrowedBookIds", orderService.findActiveBorrowedBookIdsByUser(userId));
        return response;
    }
}