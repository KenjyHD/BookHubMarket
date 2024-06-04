package com.kenjy.bookapi.repository;

import com.kenjy.bookapi.entities.Book;
import com.kenjy.bookapi.entities.UsersBooks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UsersBooksRepository extends JpaRepository<UsersBooks, Long> {
    List<UsersBooks> findByUserId(Long userId);
    @Query("SELECT ub.book FROM UsersBooks ub WHERE ub.user.id = :userId AND LOWER(ub.book.title) LIKE LOWER(CONCAT('%', :text, '%')) ORDER BY ub.book.title")
    List<Book> findByUserIdAndBookTitleContainingIgnoreCaseOrderByTitle(@Param("userId") Long userId, @Param("text") String text);
}
