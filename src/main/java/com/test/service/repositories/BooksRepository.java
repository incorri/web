package com.test.service.repositories;

import com.test.service.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface BooksRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByAuthorName(String authorName);
    Optional<Book> findOneByTitleName(String titleName);
}
