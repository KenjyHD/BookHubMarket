package com.kenjy.bookapi.repository;

import com.kenjy.bookapi.entities.Book;
import com.kenjy.bookapi.entities.UsersBooks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UsersBooksRepository extends JpaRepository<UsersBooks, Long> {
    @Query("SELECT ub.book FROM UsersBooks ub WHERE ub.user.id = :userId AND " +
            "(:title IS NULL OR LOWER(ub.book.title) LIKE %:title%) AND " +
            "(:genre IS NULL OR LOWER(ub.book.genre) LIKE %:genre%) AND " +
            "(:author IS NULL OR LOWER(ub.book.authorName) LIKE %:author%) AND " +
            "(:minPrice IS NULL OR ub.book.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR ub.book.price <= :maxPrice) " +
            "ORDER BY ub.book.title")
    List<Book> findPurchasedBooks(
            @Param("userId") Long userId,
            @Param("title") String title,
            @Param("genre") String genre,
            @Param("author") String author,
            @Param("minPrice") Float minPrice,
            @Param("maxPrice") Float maxPrice
    );
}
