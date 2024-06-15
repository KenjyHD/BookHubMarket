package com.kenjy.bookapi.repository;

import com.kenjy.bookapi.entities.AuthorRequest;
import com.kenjy.bookapi.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AuthorRequestRepository extends JpaRepository<AuthorRequest, Long> {
    @Query("SELECT ar FROM AuthorRequest ar WHERE ar.book.id = :bookId AND ar.status = :status")
    Optional<AuthorRequest> findByBookIdAndStatus(@Param("bookId") Long bookId, @Param("status") RequestStatus status);

    boolean existsByBookIdAndStatus(@Param("bookId") Long bookId, @Param("status") RequestStatus status);

    @Query("SELECT count(ar) > 0 FROM AuthorRequest ar WHERE ar.user.id = :userId AND ar.status = :status")
    boolean existsByUserIdAndStatus(@Param("userId") Long userId, @Param("status") RequestStatus status);
}
