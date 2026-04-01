package com.example.bookexchange.service.impl;

import com.example.bookexchange.dto.BookRequest;
import com.example.bookexchange.dto.BookResponse;
import com.example.bookexchange.entity.Book;
import com.example.bookexchange.entity.RoleName;
import com.example.bookexchange.entity.User;
import com.example.bookexchange.exception.BadRequestException;
import com.example.bookexchange.exception.ResourceNotFoundException;
import com.example.bookexchange.repository.BookRepository;
import com.example.bookexchange.repository.UserRepository;
import com.example.bookexchange.service.BookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public BookServiceImpl(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Override
    public BookResponse create(BookRequest request) {
        User seller = userRepository.findById(request.getSellerId())
            .orElseThrow(() -> new ResourceNotFoundException("Seller not found with id " + request.getSellerId()));
        validateSellerRole(seller);

        Book book = Book.builder()
            .title(request.getTitle())
            .author(request.getAuthor())
            .price(request.getPrice())
            .status(request.getStatus())
            .seller(seller)
            .build();

        Book savedBook = bookRepository.save(book);
        return mapToResponse(savedBook);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> findAll() {
        return bookRepository.findAll()
            .stream()
            .map(this::mapToResponse)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponse findById(Long id) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + id));

        return mapToResponse(book);
    }

    @Override
    public BookResponse update(Long id, BookRequest request) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + id));

        User seller = userRepository.findById(request.getSellerId())
            .orElseThrow(() -> new ResourceNotFoundException("Seller not found with id " + request.getSellerId()));
        validateSellerRole(seller);

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setPrice(request.getPrice());
        book.setStatus(request.getStatus());
        book.setSeller(seller);

        Book updatedBook = bookRepository.save(book);
        return mapToResponse(updatedBook);
    }

    @Override
    public void delete(Long id) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + id));

        bookRepository.delete(book);
    }

    private void validateSellerRole(User seller) {
        boolean isSeller = seller.getRoles().stream()
            .anyMatch(role -> role.getName() == RoleName.SELLER || role.getName() == RoleName.ADMIN);
        if (!isSeller) {
            throw new BadRequestException("Selected user is not allowed to sell books");
        }
    }

    private BookResponse mapToResponse(Book book) {
        User seller = book.getSeller();

        return BookResponse.builder()
            .id(book.getId())
            .title(book.getTitle())
            .author(book.getAuthor())
            .price(book.getPrice())
            .status(book.getStatus())
            .sellerId(seller != null ? seller.getId() : null)
            .sellerName(seller != null ? seller.getFullName() : null)
            .build();
    }
}
