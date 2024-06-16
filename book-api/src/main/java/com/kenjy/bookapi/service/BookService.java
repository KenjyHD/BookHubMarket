package com.kenjy.bookapi.service;

import com.kenjy.bookapi.dto.AddBookDTO;
import com.kenjy.bookapi.dto.GetBookDTO;
import com.kenjy.bookapi.entities.*;
import com.kenjy.bookapi.enums.RequestStatus;
import com.kenjy.bookapi.exception.BookNotFoundException;
import com.kenjy.bookapi.mapper.BookMapper;
import com.kenjy.bookapi.repository.BookContentRepository;
import com.kenjy.bookapi.repository.BookCoverRepository;
import com.kenjy.bookapi.repository.BookRepository;
import com.kenjy.bookapi.repository.UsersBooksRepository;
import com.kenjy.bookapi.security.WebSecurityConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final UsersBooksRepository usersBooksRepository;
    private final AuthorRequestService authorRequestService;
    private final BookMapper bookMapper;
    private final BookContentRepository bookContentRepository;
    private final BookCoverRepository bookCoverRepository;

    private final String contentFolder;
    private final String coverFolder;

    public BookService(BookRepository bookRepository, UsersBooksRepository usersBooksRepository, AuthorRequestService authorRequestService, BookMapper bookMapper, BookContentRepository bookContentRepository, BookCoverRepository bookCoverRepository, @Value("${content-folder}") String contentFolderPath,
                       @Value("${cover-folder}") String coverFolderPath) {
        this.bookRepository = bookRepository;
        this.usersBooksRepository = usersBooksRepository;
        this.authorRequestService = authorRequestService;
        this.bookMapper = bookMapper;
        this.bookContentRepository = bookContentRepository;
        this.bookCoverRepository = bookCoverRepository;
        this.contentFolder = System.getProperty("user.dir") + contentFolderPath;
        this.coverFolder = System.getProperty("user.dir") + coverFolderPath;
    }

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    public List<GetBookDTO> getAllVerifiedBooks(String title, String genre, String author, Float minPrice, Float maxPrice) {
        String titleLowercase = title != null ? title.toLowerCase() : null;
        String genreLowercase = genre != null ? genre.toLowerCase() : null;
        String authorLowercase = author != null ? author.toLowerCase() : null;
        List<Book> books = bookRepository.findBooks(titleLowercase, genreLowercase, authorLowercase, minPrice, maxPrice);
        return books.stream()
                .filter(book -> !authorRequestService.existsAuthorRequestByBookIdAndStatus(book.getId(), RequestStatus.PENDING))
                .map(bookMapper::toBookDTO)
                .collect(Collectors.toList());
    }

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

    public Book updateBook(User user, Long bookId, AddBookDTO dto, MultipartFile bookContentFile, MultipartFile bookCoverFile) throws IOException {
        Book book = validateAndGetBook(bookId);

        if (!user.getId().equals(book.getAuthor().getId()) && !user.getRole().equals(WebSecurityConfig.ADMIN)) {
            throw new RuntimeException("You are not authorized to update this book.");
        }

        book.setTitle(dto.getTitle());
        book.setAuthorName(dto.getAuthor());
        book.setGenre(dto.getGenre());
        book.setDescription(dto.getDescription());
        book.setPrice(dto.getPrice());

        if (bookContentFile != null) {
            UUID oldFileId = book.getBookContent().getId();
            book.setBookContent(saveBookContentFile(bookContentFile));
            deleteContentFile(oldFileId);
        }

        if (bookCoverFile != null) {
            UUID oldFileId = book.getBookCover().getId();
            book.setBookCover(saveBookCoverFile(bookCoverFile));
            deleteCoverFile(oldFileId);
        }

        return bookRepository.save(book);
    }

    private void deleteContentFile(UUID fileId) {
        bookContentRepository.deleteById(fileId);
        deleteFile(contentFolder + fileId);
    }

    private void deleteCoverFile(UUID fileId) {
        bookCoverRepository.deleteById(fileId);
        deleteFile(coverFolder + fileId);
    }

    private void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    private BookContent saveBookContentFile(MultipartFile file) throws IOException {
        BookContent bookContent = new BookContent();
        bookContent.setMimeType(file.getContentType());
        bookContent.setFileName(file.getOriginalFilename());
        BookContent savedBookContent = bookContentRepository.save(bookContent);

        File saveFile = new File(contentFolder + savedBookContent.getId());
        try (OutputStream os = new FileOutputStream(saveFile)) {
            os.write(file.getBytes());
        }

        return bookContent;
    }

    private BookCover saveBookCoverFile(MultipartFile file) throws IOException {
        BookCover bookCover = new BookCover();
        bookCover.setMimeType(file.getContentType());
        bookCover.setFileName(file.getOriginalFilename());
        BookCover savedBookCover = bookCoverRepository.save(bookCover);

        File saveFile = new File(coverFolder + savedBookCover.getId());
        try (OutputStream os = new FileOutputStream(saveFile)) {
            os.write(file.getBytes());
        }

        return bookCover;
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

    public List<GetBookDTO> getPurchasedBooks(Long userId, String title, String genre, String author, Float minPrice, Float maxPrice) {
        String titleLowercase = title != null ? title.toLowerCase() : null;
        String genreLowercase = genre != null ? genre.toLowerCase() : null;
        String authorLowercase = author != null ? author.toLowerCase() : null;
        List<Book> books = usersBooksRepository.findPurchasedBooks(userId, titleLowercase, genreLowercase, authorLowercase, minPrice, maxPrice);
        return books.stream().map(bookMapper::toBookDTO).collect(Collectors.toList());
    }

    public List<GetBookDTO> getAuthorBooks(Long authorId, String title, String genre, String author, Float minPrice, Float maxPrice) {
        String titleLowercase = title != null ? title.toLowerCase() : null;
        String genreLowercase = genre != null ? genre.toLowerCase() : null;
        String authorLowercase = author != null ? author.toLowerCase() : null;
        List<Book> books = bookRepository.findAuthorBooks(authorId, titleLowercase, genreLowercase, authorLowercase, minPrice, maxPrice);
        return books.stream().map(bookMapper::toBookDTO).collect(Collectors.toList());
    }

    public Book findBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(String.format("Book with id {%s} not found", bookId)));
    }
}
