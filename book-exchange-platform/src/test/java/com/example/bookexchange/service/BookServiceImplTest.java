package com.example.bookexchange.service;

import com.example.bookexchange.dto.BookRequest;
import com.example.bookexchange.entity.Book;
import com.example.bookexchange.entity.BookStatus;
import com.example.bookexchange.entity.Role;
import com.example.bookexchange.entity.RoleName;
import com.example.bookexchange.entity.User;
import com.example.bookexchange.exception.BadRequestException;
import com.example.bookexchange.exception.ResourceNotFoundException;
import com.example.bookexchange.repository.BookRepository;
import com.example.bookexchange.repository.UserRepository;
import com.example.bookexchange.service.impl.BookServiceImpl;
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
class BookServiceImplTest {
    @Mock BookRepository bookRepository;
    @Mock UserRepository userRepository;
    @InjectMocks BookServiceImpl bookService;

    BookRequest request;
    User seller;

    @BeforeEach
    void setUp() {
        seller = User.builder().id(1L).fullName("Seller").roles(java.util.Set.of(new Role(RoleName.SELLER))).build();
        request = new BookRequest();
        request.setTitle("Spring in Action");
        request.setAuthor("Craig Walls");
        request.setPrice(BigDecimal.valueOf(300));
        request.setStatus(BookStatus.AVAILABLE);
        request.setSellerId(1L);
    }

    @Test void create_shouldSaveBook() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(seller));
        when(bookRepository.save(any(Book.class))).thenAnswer(i -> { Book b=i.getArgument(0); b.setId(11L); return b;});
        var response = bookService.create(request);
        assertEquals(11L, response.getId());
    }

    @Test void create_shouldThrowWhenSellerMissing() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> bookService.create(request));
    }


    @Test void create_shouldRejectNonSellerUser() {
        User buyerOnly = User.builder().id(1L).fullName("Buyer").roles(java.util.Set.of(new Role(RoleName.BUYER))).build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(buyerOnly));
        assertThrows(BadRequestException.class, () -> bookService.create(request));
    }

    @Test void findAll_shouldReturnBooks() {
        when(bookRepository.findAll()).thenReturn(List.of(Book.builder().id(1L).title("T").author("A").price(BigDecimal.ONE).status(BookStatus.AVAILABLE).seller(seller).build()));
        assertEquals(1, bookService.findAll().size());
    }

    @Test void findById_shouldReturnBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(Book.builder().id(1L).title("T").author("A").price(BigDecimal.ONE).status(BookStatus.AVAILABLE).seller(seller).build()));
        assertEquals("T", bookService.findById(1L).getTitle());
    }

    @Test void findById_shouldThrowWhenMissing() {
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> bookService.findById(2L));
    }

    @Test void update_shouldModifyBook() {
        Book book = Book.builder().id(5L).title("Old").author("Old").price(BigDecimal.ONE).status(BookStatus.AVAILABLE).seller(seller).build();
        when(bookRepository.findById(5L)).thenReturn(Optional.of(book));
        when(userRepository.findById(1L)).thenReturn(Optional.of(seller));
        when(bookRepository.save(any(Book.class))).thenAnswer(i -> i.getArgument(0));
        var response = bookService.update(5L, request);
        assertEquals("Spring in Action", response.getTitle());
    }

    @Test void update_shouldThrowWhenBookMissing() {
        when(bookRepository.findById(5L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> bookService.update(5L, request));
    }

    @Test void delete_shouldRemoveBook() {
        Book book = Book.builder().id(5L).seller(seller).title("X").author("Y").price(BigDecimal.ONE).status(BookStatus.AVAILABLE).build();
        when(bookRepository.findById(5L)).thenReturn(Optional.of(book));
        bookService.delete(5L);
        verify(bookRepository).delete(book);
    }
}
