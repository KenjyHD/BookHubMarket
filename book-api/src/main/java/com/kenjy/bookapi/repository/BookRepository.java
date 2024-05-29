package com.kenjy.bookapi.repository;

import com.kenjy.bookapi.entities.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Books, Long> {

    List<Books> findAllByOrderByTitle();

    List<Books> findByTitleContainingIgnoreCaseOrderByTitle(String title);
}
