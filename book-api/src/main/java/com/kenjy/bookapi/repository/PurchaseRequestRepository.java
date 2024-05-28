package com.kenjy.bookapi.repository;


import com.kenjy.bookapi.entities.PurchaseRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRequestRepository extends JpaRepository<PurchaseRequest, Long> {
    boolean existsByUserIdAndBookId(Long userId, Long bookId);
}
