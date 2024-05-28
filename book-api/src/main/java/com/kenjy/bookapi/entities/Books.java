package com.kenjy.bookapi.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "books")
public class Books {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String author;

    @NotNull
    private Float price;

    private String genre;

    private String description;

    @NotNull
    private String bookPdfPath;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UsersBooks> usersBooks = new HashSet<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PurchaseRequest> purchaseRequests = new HashSet<>();

    public Books(String title, String author, Float price, String bookPdfPath) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.bookPdfPath = bookPdfPath;
    }
}
