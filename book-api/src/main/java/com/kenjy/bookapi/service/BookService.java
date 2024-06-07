package com.kenjy.bookapi.service;

import com.kenjy.bookapi.dto.AddBookDTO;
import com.kenjy.bookapi.dto.GetBookDTO;
import com.kenjy.bookapi.entities.*;
import com.kenjy.bookapi.enums.RequestStatus;
import com.kenjy.bookapi.exception.BookNotFoundException;
import com.kenjy.bookapi.mapper.BookMapper;
import com.kenjy.bookapi.repository.BookRepository;
import com.kenjy.bookapi.repository.UsersBooksRepository;
import com.kenjy.bookapi.security.WebSecurityConfig;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookService {
    private final BookRepository bookRepository;
    private final UsersBooksRepository usersBooksRepository;
    private final AuthorRequestService authorRequestService;
    private final BookMapper bookMapper;

    private final String contentFolder = System.getProperty("user.dir") + "\\book-api\\src\\main\\resources\\file-storage\\book-contents\\";
    private final String coverFolder = System.getProperty("user.dir") + "\\book-api\\src\\main\\resources\\file-storage\\book-covers\\";

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    public List<Book> getBooks() {
        return bookRepository.findAllByOrderByTitle();
    }

    public List<Book> getBooksContainingText(String text) {
        return bookRepository.findAllByTitleContainingIgnoreCaseOrderByTitle(text);
    }

    public Book validateAndGetBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(String.format("Book with id %s not found", id)));
    }

    public void deleteBook(Book book) {
        bookRepository.delete(book);
    }

    public GetBookDTO createBook(User user, AddBookDTO dto, MultipartFile bookContentFile, MultipartFile bookCoverFile) {
        try {
            Book book = bookMapper.toBook(user, dto, bookContentFile, bookCoverFile);

            File file = new File(contentFolder + book.getBookContent().getId());
            try (OutputStream os = new FileOutputStream(file)) {
                os.write(bookContentFile.getBytes());
            }

            File coverFile = new File(coverFolder + book.getBookCover().getId());
            try (OutputStream os = new FileOutputStream(coverFile)) {
                os.write(bookCoverFile.getBytes());
            }

            if (user.getRole().equals(WebSecurityConfig.AUTHOR)) {
                book.setAuthor(user);
            }
            Book savedBook = bookRepository.save(book);

            if (user.getRole().equals(WebSecurityConfig.USER)) {
                AuthorRequest authorRequest = new AuthorRequest();
                authorRequest.setUser(user);
                authorRequest.setBook(savedBook);
                authorRequest.setStatus(RequestStatus.PENDING);
                authorRequest.setRequestDate(LocalDateTime.now());

                authorRequestService.saveAuthorRequest(authorRequest);
            }

            return bookMapper.toBookDTO(savedBook);
        } catch (IOException e) {
            logger.error("An error occurred:", e);
            throw new RuntimeException("Failed to save file");
        }
    }

    public Resource getBookContent(Long bookId) {
        try {
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new RuntimeException("Book not found"));

            BookContent bookContent = book.getBookContent();
            if (bookContent == null) {
                throw new RuntimeException("Book content not found");
            }

            Path filePath = Paths.get(contentFolder + bookContent.getId()).normalize();
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

    public BookContent getBookContentDetails(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(String.format("Book with id {%s} not found", bookId)));
        return book.getBookContent();
    }

    public List<GetBookDTO> getPurchasedBooks(Long userId, String text) {
        List<Book> books;
        if (text != null && !text.isEmpty()) {
            books = usersBooksRepository.findByUserIdAndBookTitleContainingIgnoreCaseOrderByTitle(userId, text);
        } else {
            List<UsersBooks> usersBooks = usersBooksRepository.findByUserId(userId);
            books = usersBooks.stream().map(UsersBooks::getBook).collect(Collectors.toList());
        }
        return books.stream().map(bookMapper::toBookDTO).collect(Collectors.toList());
    }

    public List<GetBookDTO> getAuthorBooks(Long authorId, String text) {
        List<Book> books;
        if (text != null && !text.isEmpty()) {
            books = bookRepository.findAllByAuthorIdAndTitleContainingIgnoreCaseOrderByTitle(authorId, text);
        } else {
            books = bookRepository.findAllByAuthorId(authorId);
        }

        return books.stream().map(bookMapper::toBookDTO).collect(Collectors.toList());
    }

    public Book findBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(String.format("Book with id {%s} not found", bookId)));
    }
}
