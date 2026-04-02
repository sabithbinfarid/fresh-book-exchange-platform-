package com.example.bookexchange.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequest {
    @NotNull
    private Long bookId;
    private Long buyerId;
}
