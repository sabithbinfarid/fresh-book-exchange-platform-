package com.example.bookexchange.service;

import com.example.bookexchange.dto.RegisterRequest;
import com.example.bookexchange.dto.UserResponse;
import com.example.bookexchange.dto.UserUpdateRequest;

import java.util.List;

public interface UserService {
    UserResponse register(RegisterRequest request);
    List<UserResponse> findAll();
    UserResponse findById(Long id);
    UserResponse update(Long id, UserUpdateRequest request);
    void delete(Long id);
}
