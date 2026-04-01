package com.example.bookexchange.service;

import com.example.bookexchange.dto.BookRequest;
import com.example.bookexchange.dto.BookResponse;

import java.util.List;

public interface BookService {
    BookResponse create(BookRequest request);
    List<BookResponse> findAll();
    BookResponse findById(Long id);
    BookResponse update(Long id, BookRequest request);
    void delete(Long id);
}
