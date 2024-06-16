package com.kenjy.bookapi.repository;

import com.kenjy.bookapi.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByOrderByTitle();

    List<Book> findAllByTitleContainingIgnoreCaseOrderByTitle(String text);

    @Query("SELECT b FROM Book b WHERE " +
            "(:title IS NULL OR LOWER(b.title) LIKE %:title%) AND " +
            "(:genre IS NULL OR LOWER(b.genre) LIKE %:genre%) AND " +
            "(:author IS NULL OR LOWER(b.authorName) LIKE %:author%) AND " +
            "(:minPrice IS NULL OR b.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR b.price <= :maxPrice)")
    List<Book> findBooks(
            @Param("title") String title,
            @Param("genre") String genre,
            @Param("author") String author,
            @Param("minPrice") Float minPrice,
            @Param("maxPrice") Float maxPrice
    );

    @Query("SELECT b FROM Book b WHERE b.author.id = :authorId AND " +
            "(:title IS NULL OR LOWER(b.title) LIKE %:title%) AND " +
            "(:genre IS NULL OR LOWER(b.genre) LIKE %:genre%) AND " +
            "(:author IS NULL OR LOWER(b.authorName) LIKE %:author%) AND " +
            "(:minPrice IS NULL OR b.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR b.price <= :maxPrice)")
    List<Book> findAuthorBooks(
            @Param("authorId") Long authorId,
            @Param("title") String title,
            @Param("genre") String genre,
            @Param("author") String author,
            @Param("minPrice") Float minPrice,
            @Param("maxPrice") Float maxPrice
    );
}
