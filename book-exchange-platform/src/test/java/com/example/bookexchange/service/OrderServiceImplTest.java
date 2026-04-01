package com.example.bookexchange.service;

import com.example.bookexchange.dto.OrderRequest;
import com.example.bookexchange.entity.*;
import com.example.bookexchange.exception.BadRequestException;
import com.example.bookexchange.exception.ResourceNotFoundException;
import com.example.bookexchange.repository.BookOrderRepository;
import com.example.bookexchange.repository.BookRepository;
import com.example.bookexchange.repository.UserRepository;
import com.example.bookexchange.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @Mock BookOrderRepository orderRepository;
    @Mock BookRepository bookRepository;
    @Mock UserRepository userRepository;
    @InjectMocks OrderServiceImpl orderService;

    OrderRequest request;
    Book book;
    User buyer;

    @BeforeEach
    void setUp() {
        buyer = User.builder().id(2L).fullName("Buyer").roles(java.util.Set.of(new Role(RoleName.BUYER))).build();
        User seller = User.builder().id(1L).fullName("Seller").roles(java.util.Set.of(new Role(RoleName.SELLER))).build();
        book = Book.builder().id(1L).title("Book A").author("Auth").price(BigDecimal.TEN).status(BookStatus.AVAILABLE).seller(seller).build();
        request = new OrderRequest();
        request.setBookId(1L);
        request.setBuyerId(2L);
    }

    @Test void create_shouldCreateOrderAndReserveBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(userRepository.findById(2L)).thenReturn(Optional.of(buyer));
        when(orderRepository.save(any(BookOrder.class))).thenAnswer(i -> { BookOrder o=i.getArgument(0); o.setId(7L); return o;});
        var response = orderService.create(request);
        assertEquals(7L, response.getId());
        assertEquals(BookStatus.RESERVED, book.getStatus());
    }

    @Test void create_shouldThrowWhenBookMissing() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> orderService.create(request));
    }

    @Test void create_shouldThrowWhenBookUnavailable() {
        book.setStatus(BookStatus.SOLD);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        assertThrows(BadRequestException.class, () -> orderService.create(request));
    }

    @Test void create_shouldThrowWhenBuyerMissing() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> orderService.create(request));
    }


    @Test void create_shouldRejectNonBuyerUser() {
        User sellerOnly = User.builder().id(2L).fullName("Seller").roles(java.util.Set.of(new Role(RoleName.SELLER))).build();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(userRepository.findById(2L)).thenReturn(Optional.of(sellerOnly));
        assertThrows(BadRequestException.class, () -> orderService.create(request));
    }

    @Test void findAll_shouldReturnOrders() {
        BookOrder order = BookOrder.builder().id(1L).book(book).buyer(buyer).status(OrderStatus.PENDING).orderedAt(java.time.LocalDateTime.now()).build();
        when(orderRepository.findAll()).thenReturn(List.of(order));
        assertEquals(1, orderService.findAll().size());
    }

    @Test void updateStatus_shouldCompleteOrderAndSellBook() {
        BookOrder order = BookOrder.builder().id(1L).book(book).buyer(buyer).status(OrderStatus.PENDING).orderedAt(java.time.LocalDateTime.now()).build();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(BookOrder.class))).thenAnswer(i -> i.getArgument(0));
        var response = orderService.updateStatus(1L, OrderStatus.COMPLETED);
        assertEquals(OrderStatus.COMPLETED, response.getStatus());
        assertEquals(BookStatus.SOLD, book.getStatus());
    }

    @Test void updateStatus_shouldThrowWhenOrderMissing() {
        when(orderRepository.findById(10L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> orderService.updateStatus(10L, OrderStatus.APPROVED));
    }
}
