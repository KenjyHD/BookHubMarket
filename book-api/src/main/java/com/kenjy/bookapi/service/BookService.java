package com.kenjy.bookapi.service;

import com.kenjy.bookapi.entities.Book;

import java.util.List;

public interface BookService {

    List<Book> getBooks();

    List<Book> getBooksContainingText(String text);

    Book validateAndGetBook(Long id);

    Book saveBook(Book book);

    void deleteBook(Book book);
}
