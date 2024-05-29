package com.kenjy.bookapi.service;

import com.kenjy.bookapi.entities.Books;
import org.springframework.core.io.Resource;

import java.util.List;

public interface BookService {

    List<Books> getBooks();

    List<Books> getBooksContainingText(String text);

    Books validateAndGetBook(Long id);

    Books saveBook(Books book);

    void deleteBook(Books book);

    Resource getBookPdf(Long bookId);

    List<Books> getPurchasedBooks(Long userId);
}
