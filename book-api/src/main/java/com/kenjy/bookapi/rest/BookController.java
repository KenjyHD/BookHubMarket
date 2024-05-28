package com.kenjy.bookapi.rest;

import com.kenjy.bookapi.entities.Books;
import com.kenjy.bookapi.mapper.BookMapper;
import com.kenjy.bookapi.dto.AddBookDTO;
import com.kenjy.bookapi.dto.GetBookDTO;
import com.kenjy.bookapi.service.BookService;
import com.kenjy.bookapi.config.SwaggerConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;
    private static final Logger logger = LoggerFactory.getLogger(BookController.class);
    private final String uploadDir = System.getProperty("user.dir") + "\\book-api\\src\\main\\resources\\file-storage\\";

    @Operation(security = {@SecurityRequirement(name = SwaggerConfig.BASIC_AUTH_SECURITY_SCHEME)})
    @GetMapping
    public List<GetBookDTO> getBooks(@RequestParam(value = "text", required = false) String text) {
        List<Books> books = (text == null) ? bookService.getBooks() : bookService.getBooksContainingText(text);
        return books.stream()
                .map(bookMapper::toBookDTO)
                .collect(Collectors.toList());
    }

    @Operation(security = {@SecurityRequirement(name = SwaggerConfig.BASIC_AUTH_SECURITY_SCHEME)})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = {"multipart/form-data"})
    public GetBookDTO createBook(@RequestPart("book") AddBookDTO dto,
                                 @RequestPart("pdfFile") MultipartFile pdfFile) {
        try {
            File file = new File(uploadDir + pdfFile.getOriginalFilename());
            try (OutputStream os = new FileOutputStream(file)) {
                os.write(pdfFile.getBytes());
            }

            Books book = bookMapper.toBook(dto, pdfFile);
            return bookMapper.toBookDTO(bookService.saveBook(book));
        } catch (IOException e) {
            logger.error("An error occurred:", e);
            throw new RuntimeException("Failed to save PDF file");
        }
    }

    @Operation(security = {@SecurityRequirement(name = SwaggerConfig.BASIC_AUTH_SECURITY_SCHEME)})
    @DeleteMapping("/{id}")
    public GetBookDTO deleteBook(@PathVariable Long id) {
        Books book = bookService.validateAndGetBook(id);
        bookService.deleteBook(book);
        return bookMapper.toBookDTO(book);
    }

    @Operation(security = {@SecurityRequirement(name = SwaggerConfig.BASIC_AUTH_SECURITY_SCHEME)})
    @GetMapping("/{id}")
    public GetBookDTO getBook(@PathVariable Long id) {
        Books book = bookService.validateAndGetBook(id);
        return bookMapper.toBookDTO(book);
    }

    @GetMapping("/purchased/{userId}")
    public ResponseEntity<List<Books>> getPurchasedBooks(@PathVariable Long userId) {
        List<Books> purchasedBooks = bookService.getPurchasedBooks(userId);
        return ResponseEntity.ok(purchasedBooks);
    }

    @GetMapping("/{bookId}/download")
    public ResponseEntity<Resource> downloadBook(@PathVariable Long bookId) {
        Resource resource = bookService.getBookPdf(bookId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"book_" + bookId + ".pdf\"")
                .body(resource);
    }

}
