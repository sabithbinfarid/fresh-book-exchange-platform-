package com.example.bookexchange.dto;

import com.example.bookexchange.entity.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class OrderResponse {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private Long buyerId;
    private String buyerName;
    private OrderStatus status;
    private LocalDateTime orderedAt;
}
