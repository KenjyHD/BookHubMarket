package com.kenjy.bookapi.repository;

import com.kenjy.bookapi.entities.BookContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookContentRepository extends JpaRepository<BookContent, UUID> {
}
