package com.kenjy.bookapi.repository;

import com.kenjy.bookapi.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByOrderByTitle();

    List<Book> findAllByTitleContainingIgnoreCaseOrderByTitle(String text);

    List<Book> findAllByAuthorIdAndTitleContainingIgnoreCaseOrderByTitle(Long authorId, String text);

    List<Book> findAllByAuthorId(Long authorId);
}
