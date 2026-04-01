package com.example.bookexchange.dto;

import com.example.bookexchange.entity.BookStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class BookResponse {
    private Long id;
    private String title;
    private String author;
    private BigDecimal price;
    private BookStatus status;
    private Long sellerId;
    private String sellerName;
}
