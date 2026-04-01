package com.example.bookexchange.dto;

import com.example.bookexchange.entity.BookStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BookRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String author;
    @NotNull
    @DecimalMin("0.0")
    private BigDecimal price;
    @NotNull
    private BookStatus status;
    @NotNull
    private Long sellerId;
}
