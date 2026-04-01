package com.example.bookexchange.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class UserResponse {
    private Long id;
    private String fullName;
    private String email;
    private boolean enabled;
    private Set<String> roles;
}
