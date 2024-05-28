package com.kenjy.bookapi.repository;

import com.kenjy.bookapi.entities.UsersBooks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsersBooksRepository extends JpaRepository<UsersBooks, Long> {
    List<UsersBooks> findByUserId(Long userId);
}
