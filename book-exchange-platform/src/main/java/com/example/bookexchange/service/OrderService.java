package com.example.bookexchange.service;

import com.example.bookexchange.dto.OrderRequest;
import com.example.bookexchange.dto.OrderResponse;
import com.example.bookexchange.entity.OrderStatus;

import java.util.List;

public interface OrderService {
    OrderResponse create(OrderRequest request);
    List<OrderResponse> findAll();
    OrderResponse findById(Long id);
    OrderResponse updateStatus(Long id, OrderStatus status);
    void delete(Long id);
}
