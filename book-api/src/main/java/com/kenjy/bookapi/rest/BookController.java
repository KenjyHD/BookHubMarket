package com.kenjy.bookapi.rest;

import com.kenjy.bookapi.config.SwaggerConfig;
import com.kenjy.bookapi.dto.AddBookDTO;
import com.kenjy.bookapi.dto.GetBookDTO;
import com.kenjy.bookapi.entities.Book;
import com.kenjy.bookapi.entities.BookContent;
import com.kenjy.bookapi.entities.User;
import com.kenjy.bookapi.mapper.BookMapper;
import com.kenjy.bookapi.service.BookService;
import com.kenjy.bookapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;
    private final UserService userService;
    private final BookMapper bookMapper;

    @Operation(security = {@SecurityRequirement(name = SwaggerConfig.BASIC_AUTH_SECURITY_SCHEME)})
    @GetMapping
    public List<GetBookDTO> getBooks(@RequestParam(value = "text", required = false) String text) {
        List<Book> books = (text == null) ? bookService.getBooks() : bookService.getBooksContainingText(text);
        return books.stream()
                .map(bookMapper::toBookDTO)
                .collect(Collectors.toList());
    }

    @Operation(security = {@SecurityRequirement(name = SwaggerConfig.BASIC_AUTH_SECURITY_SCHEME)})
    @GetMapping("/purchased/{userId}")
    public ResponseEntity<List<GetBookDTO>> getPurchasedBooks(
            @PathVariable Long userId,
            @RequestParam(value = "text", required = false) String text) {
        return ResponseEntity.ok(bookService.getPurchasedBooks(userId, text));
    }

    @Operation(security = {@SecurityRequirement(name = SwaggerConfig.BASIC_AUTH_SECURITY_SCHEME)})
    @GetMapping("/author/{userId}")
    public ResponseEntity<List<GetBookDTO>> getAuthorBooks(
            @PathVariable Long userId,
            @RequestParam(value = "text", required = false) String text) {
        return ResponseEntity.ok(bookService.getAuthorBooks(userId, text));
    }

    @Operation(security = {@SecurityRequirement(name = SwaggerConfig.BASIC_AUTH_SECURITY_SCHEME)})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = {"multipart/form-data"})
    public GetBookDTO createBook(@AuthenticationPrincipal UserDetails userDetails,
                                 @RequestPart("book") AddBookDTO dto,
                                 @RequestPart("bookContent") MultipartFile bookContentFile,
                                 @RequestPart("bookCover") MultipartFile bookCoverFile) {
        User user = userService.findByUsername(userDetails.getUsername());
        return bookService.createBook(user, dto, bookContentFile, bookCoverFile);
    }

    @Operation(security = {@SecurityRequirement(name = SwaggerConfig.BASIC_AUTH_SECURITY_SCHEME)})
    @DeleteMapping("/{id}")
    public GetBookDTO deleteBook(@PathVariable Long id) {
        Book book = bookService.validateAndGetBook(id);
        bookService.deleteBook(book);
        return bookMapper.toBookDTO(book);
    }

    @Operation(security = {@SecurityRequirement(name = SwaggerConfig.BASIC_AUTH_SECURITY_SCHEME)})
    @GetMapping("/{id}")
    public ResponseEntity<GetBookDTO> getBook(@PathVariable Long id) {
        Book book = bookService.validateAndGetBook(id);
        return ResponseEntity.ok(bookMapper.toBookDTO(book));
    }

    @GetMapping("/{bookId}/download")
    public ResponseEntity<Resource> downloadBook(@PathVariable Long bookId) {
        Resource resource = bookService.getBookContent(bookId);
        BookContent bookContent = bookService.getBookContentDetails(bookId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(bookContent.getMimeType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + bookContent.getFileName() + "\"")
                .body(resource);
    }
}
