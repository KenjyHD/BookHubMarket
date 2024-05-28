package com.kenjy.bookapi.entities;

import com.kenjy.bookapi.enums.PurchaseRequestStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "purchase_requests",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "book_id"})})
public class PurchaseRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Books book;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PurchaseRequestStatus status;

    @Column(nullable = false)
    private LocalDateTime requestDate;

    private LocalDateTime decisionDate;

    public PurchaseRequest(Users user, Books book, PurchaseRequestStatus status, LocalDateTime requestDate) {
        this.user = user;
        this.book = book;
        this.status = status;
        this.requestDate = requestDate;
    }
}
