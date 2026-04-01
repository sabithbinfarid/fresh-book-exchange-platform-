package com.example.bookexchange.service.impl;

import com.example.bookexchange.dto.RegisterRequest;
import com.example.bookexchange.dto.UserResponse;
import com.example.bookexchange.dto.UserUpdateRequest;
import com.example.bookexchange.entity.Role;
import com.example.bookexchange.entity.RoleName;
import com.example.bookexchange.entity.User;
import com.example.bookexchange.exception.BadRequestException;
import com.example.bookexchange.exception.ResourceNotFoundException;
import com.example.bookexchange.repository.RoleRepository;
import com.example.bookexchange.repository.UserRepository;
import com.example.bookexchange.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }
        if (request.getRoleName() == RoleName.ADMIN) {
            throw new BadRequestException("Self-registration as ADMIN is not allowed");
        }
        Role role = roleRepository.findByName(request.getRoleName())
            .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        User user = User.builder()
            .fullName(request.getFullName())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .enabled(true)
            .roles(java.util.Set.of(role))
            .build();

        return mapToResponse(userRepository.save(user));
    }

    @Override
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    public UserResponse findById(Long id) {
        return userRepository.findById(id).map(this::mapToResponse)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
    }


    @Override
    public UserResponse update(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));

        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setEnabled(request.isEnabled());

        return mapToResponse(userRepository.save(user));
    }

    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        userRepository.delete(user);
    }

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
            .id(user.getId())
            .fullName(user.getFullName())
            .email(user.getEmail())
            .enabled(user.isEnabled())
            .roles(user.getRoles().stream().map(role -> role.getName().name()).collect(java.util.stream.Collectors.toSet()))
            .build();
    }
}
