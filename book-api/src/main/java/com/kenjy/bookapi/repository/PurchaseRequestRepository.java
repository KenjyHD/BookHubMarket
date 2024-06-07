package com.kenjy.bookapi.repository;


import com.kenjy.bookapi.entities.PurchaseRequest;
import com.kenjy.bookapi.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PurchaseRequestRepository extends JpaRepository<PurchaseRequest, Long> {
    boolean existsByUserIdAndBookId(Long userId, Long bookId);

    boolean existsByUserIdAndBookIdAndStatus(Long userId, Long bookId, RequestStatus status);

    List<PurchaseRequest> findAllByBookAuthorId(Long authorId);

    @Query("SELECT pr.status FROM PurchaseRequest pr WHERE pr.user.id = :userId AND pr.book.id = :bookId")
    RequestStatus findStatusByUserIdAndBookId(Long userId, Long bookId);
}
