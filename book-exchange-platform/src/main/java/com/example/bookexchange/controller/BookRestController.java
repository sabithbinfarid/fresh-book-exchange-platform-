package com.example.bookexchange.controller;

import com.example.bookexchange.dto.BookRequest;
import com.example.bookexchange.dto.BookResponse;
import com.example.bookexchange.exception.ResourceNotFoundException;
import com.example.bookexchange.repository.UserRepository;
import com.example.bookexchange.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookRestController {
    private final BookService bookService;
    private final UserRepository userRepository;

    public BookRestController(BookService bookService, UserRepository userRepository) {
        this.bookService = bookService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<BookResponse> getAll() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public BookResponse getById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookResponse> create(@Valid @RequestBody BookRequest request, Authentication authentication) {
        if (request.getSellerId() == null) {
            request.setSellerId(resolveCurrentUserId(authentication));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public BookResponse update(@PathVariable Long id, @Valid @RequestBody BookRequest request, Authentication authentication) {
        if (request.getSellerId() == null) {
            request.setSellerId(resolveCurrentUserId(authentication));
        }
        return bookService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Long resolveCurrentUserId(Authentication authentication) {
        return userRepository.findByEmail(authentication.getName())
            .map(user -> user.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
    }
}
