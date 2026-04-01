package com.example.bookexchange.service.impl;

import com.example.bookexchange.dto.OrderRequest;
import com.example.bookexchange.dto.OrderResponse;
import com.example.bookexchange.entity.Book;
import com.example.bookexchange.entity.BookOrder;
import com.example.bookexchange.entity.BookStatus;
import com.example.bookexchange.entity.OrderStatus;
import com.example.bookexchange.entity.RoleName;
import com.example.bookexchange.entity.User;
import com.example.bookexchange.exception.BadRequestException;
import com.example.bookexchange.exception.ResourceNotFoundException;
import com.example.bookexchange.repository.BookOrderRepository;
import com.example.bookexchange.repository.BookRepository;
import com.example.bookexchange.repository.UserRepository;
import com.example.bookexchange.service.OrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final BookOrderRepository orderRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public OrderServiceImpl(BookOrderRepository orderRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Override
    public OrderResponse create(OrderRequest request) {
        Book book = bookRepository.findById(request.getBookId())
            .orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + request.getBookId()));
        if (book.getStatus() != BookStatus.AVAILABLE) {
            throw new BadRequestException("Book is not available for ordering");
        }
        User buyer = userRepository.findById(request.getBuyerId())
            .orElseThrow(() -> new ResourceNotFoundException("Buyer not found with id " + request.getBuyerId()));
        boolean isBuyer = buyer.getRoles().stream()
            .anyMatch(role -> role.getName() == RoleName.USER || role.getName() == RoleName.ADMIN);
        if (!isBuyer) {
            throw new BadRequestException("Selected user is not allowed to place orders");
        }

        book.setStatus(BookStatus.RESERVED);
        bookRepository.save(book);

        BookOrder order = BookOrder.builder()
            .book(book)
            .buyer(buyer)
            .status(OrderStatus.PENDING)
            .orderedAt(LocalDateTime.now())
            .build();
        return mapToResponse(orderRepository.save(order));
    }

    @Override
    public List<OrderResponse> findAll() {
        return orderRepository.findAll().stream().map(this::mapToResponse).toList();
    }


    @Override
    public OrderResponse findById(Long id) {
        BookOrder order = orderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));
        return mapToResponse(order);
    }

    @Override
    public OrderResponse updateStatus(Long id, OrderStatus status) {
        BookOrder order = orderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));
        order.setStatus(status);
        if (status == OrderStatus.COMPLETED) {
            order.getBook().setStatus(BookStatus.SOLD);
            bookRepository.save(order.getBook());
        }
        return mapToResponse(orderRepository.save(order));
    }


    @Override
    public void delete(Long id) {
        BookOrder order = orderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));

        if (order.getStatus() == OrderStatus.PENDING || order.getStatus() == OrderStatus.APPROVED) {
            Book book = order.getBook();
            book.setStatus(BookStatus.AVAILABLE);
            bookRepository.save(book);
        }

        orderRepository.delete(order);
    }

    private OrderResponse mapToResponse(BookOrder order) {
        return OrderResponse.builder()
            .id(order.getId())
            .bookId(order.getBook().getId())
            .bookTitle(order.getBook().getTitle())
            .buyerId(order.getBuyer().getId())
            .buyerName(order.getBuyer().getFullName())
            .status(order.getStatus())
            .orderedAt(order.getOrderedAt())
            .build();
    }
}
