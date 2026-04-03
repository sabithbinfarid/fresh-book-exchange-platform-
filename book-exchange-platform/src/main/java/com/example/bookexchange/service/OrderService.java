package com.example.bookexchange.service;

import com.example.bookexchange.dto.OrderRequest;
import com.example.bookexchange.dto.OrderResponse;
import com.example.bookexchange.entity.OrderStatus;

import java.util.List;

public interface OrderService {
    OrderResponse create(OrderRequest request);
    OrderResponse createForUser(Long bookId, String userEmail);
    List<OrderResponse> findAll();
    List<OrderResponse> findForUser(Long userId, boolean adminView);
    List<Long> findActiveBorrowedBookIdsByUser(Long userId);
    OrderResponse findById(Long id);
    OrderResponse updateStatus(Long id, OrderStatus status);
    void returnBorrow(Long id, String requesterEmail);
    void delete(Long id);
}
