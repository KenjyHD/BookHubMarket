package com.kenjy.bookapi.service;

import com.kenjy.bookapi.entities.Books;
import com.kenjy.bookapi.entities.UsersBooks;
import com.kenjy.bookapi.exception.BookNotFoundException;
import com.kenjy.bookapi.repository.BookRepository;
import com.kenjy.bookapi.repository.UsersBooksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final UsersBooksRepository usersBooksRepository;

    @Override
    public List<Books> getBooks() {
        return bookRepository.findAllByOrderByTitle();
    }

    @Override
    public List<Books> getBooksContainingText(String text) {
        return bookRepository.findByTitleContainingIgnoreCaseOrderByTitle(text);
    }

    @Override
    public Books validateAndGetBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(String.format("Books with id %s not found", id)));
    }

    @Override
    public Books saveBook(Books book) {
        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(Books book) {
        bookRepository.delete(book);
    }

    @Override
    public Resource getBookPdf(Long bookId) {
        try {
            Books book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new RuntimeException("Books not found"));

            Path filePath = Paths.get(book.getBookPdfPath()).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("File not found or not readable");
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error while trying to load file", ex);
        }
    }

    public List<Books> getPurchasedBooks(Long userId) {
        List<UsersBooks> usersBooks = usersBooksRepository.findByUserId(userId);
        return usersBooks.stream()
                .map(UsersBooks::getBook)
                .collect(Collectors.toList());
    }
}
