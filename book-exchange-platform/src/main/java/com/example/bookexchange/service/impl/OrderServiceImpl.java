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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    private static final Set<OrderStatus> ACTIVE_BORROW_STATUSES = EnumSet.of(OrderStatus.PENDING, OrderStatus.APPROVED);

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
        if (request.getBuyerId() == null) {
            throw new BadRequestException("Buyer id is required for this endpoint");
        }

        User buyer = userRepository.findById(request.getBuyerId())
            .orElseThrow(() -> new ResourceNotFoundException("Buyer not found with id " + request.getBuyerId()));

        return createBorrow(request.getBookId(), buyer);
    }

    @Override
    public OrderResponse createForUser(Long bookId, String userEmail) {
        User buyer = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with email " + userEmail));

        return createBorrow(bookId, buyer);
    }

    private OrderResponse createBorrow(Long bookId, User buyer) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + bookId));

        boolean isBuyer = buyer.getRoles().stream()
            .anyMatch(role -> role.getName() == RoleName.USER || role.getName() == RoleName.ADMIN);
        if (!isBuyer) {
            throw new BadRequestException("Selected user is not allowed to place orders");
        }

        if (orderRepository.existsByBuyer_IdAndBook_IdAndStatusIn(buyer.getId(), book.getId(), ACTIVE_BORROW_STATUSES)) {
            throw new BadRequestException("You already borrowed this book");
        }

        if (book.getStatus() != BookStatus.AVAILABLE || orderRepository.existsByBook_IdAndStatusIn(book.getId(), ACTIVE_BORROW_STATUSES)) {
            throw new BadRequestException("Book is not available for borrowing");
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
        return orderRepository.findAllByOrderByOrderedAtDesc().stream().map(this::mapToResponse).toList();
    }

    @Override
    public List<OrderResponse> findForUser(Long userId, boolean adminView) {
        List<BookOrder> orders = adminView
            ? orderRepository.findAllByOrderByOrderedAtDesc()
            : orderRepository.findAllByBuyer_IdOrderByOrderedAtDesc(userId);
        return orders.stream().map(this::mapToResponse).toList();
    }

    @Override
    public List<Long> findActiveBorrowedBookIdsByUser(Long userId) {
        return orderRepository.findBookIdsByBuyerAndStatusIn(userId, ACTIVE_BORROW_STATUSES);
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
    public void returnBorrow(Long id, String requesterEmail) {
        User requester = userRepository.findByEmail(requesterEmail)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with email " + requesterEmail));

        BookOrder order = orderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));

        boolean isAdmin = requester.getRoles().stream().anyMatch(role -> role.getName() == RoleName.ADMIN);
        if (!isAdmin && !order.getBuyer().getId().equals(requester.getId())) {
            throw new BadRequestException("You can return only your own borrow");
        }

        if (!ACTIVE_BORROW_STATUSES.contains(order.getStatus())) {
            throw new BadRequestException("Only active borrows can be returned");
        }

        Book book = order.getBook();
        book.setStatus(BookStatus.AVAILABLE);
        bookRepository.save(book);
        orderRepository.delete(order);
    }


    @Override
    public void delete(Long id) {
        BookOrder order = orderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));

        if (ACTIVE_BORROW_STATUSES.contains(order.getStatus())) {
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
