package com.kenjy.bookapi.repository;

import com.kenjy.bookapi.entities.BookCover;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookCoverRepository extends JpaRepository<BookCover, UUID> {
}
