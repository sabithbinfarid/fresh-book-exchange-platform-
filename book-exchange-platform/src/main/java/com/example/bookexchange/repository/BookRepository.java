package com.example.bookexchange.repository;

import com.example.bookexchange.entity.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Override
    @EntityGraph(attributePaths = {"seller"})
    List<Book> findAll();

    @Override
    @EntityGraph(attributePaths = {"seller"})
    Optional<Book> findById(Long id);
}
