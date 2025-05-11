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
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "Book Management", description = "Endpoints for managing books")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;
    private final UserService userService;
    private final BookMapper bookMapper;

    @Operation(
            security = @SecurityRequirement(name = SwaggerConfig.BASIC_AUTH_SECURITY_SCHEME),
            summary = "Get all books",
            description = "Retrieve books with optional filters",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved books"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            }
    )
    @GetMapping
    public ResponseEntity<List<GetBookDTO>> getBooks(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "minPrice", required = false) Float minPrice,
            @RequestParam(value = "maxPrice", required = false) Float maxPrice
    ) {
        return ResponseEntity.ok(bookService.getAllVerifiedBooks(title, genre, author, minPrice, maxPrice));
    }

    @Operation(security = {@SecurityRequirement(name = SwaggerConfig.BASIC_AUTH_SECURITY_SCHEME)})
    @GetMapping("/purchased/{userId}")
    public ResponseEntity<List<GetBookDTO>> getPurchasedBooks(
            @PathVariable Long userId,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "minPrice", required = false) Float minPrice,
            @RequestParam(value = "maxPrice", required = false) Float maxPrice
    ) {
        return ResponseEntity.ok(bookService.getPurchasedBooks(userId, title, genre, author, minPrice, maxPrice));
    }

    @Operation(security = {@SecurityRequirement(name = SwaggerConfig.BASIC_AUTH_SECURITY_SCHEME)})
    @GetMapping("/author/{userId}")
    public ResponseEntity<List<GetBookDTO>> getAuthorBooks(
            @PathVariable Long userId,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "minPrice", required = false) Float minPrice,
            @RequestParam(value = "maxPrice", required = false) Float maxPrice
    ) {
        return ResponseEntity.ok(bookService.getAuthorBooks(userId, title, genre, author, minPrice, maxPrice));
    }

    @Operation(
            security = @SecurityRequirement(name = SwaggerConfig.BASIC_AUTH_SECURITY_SCHEME),
            summary = "Create a new book",
            description = "Creates a new book with content and cover image",
            tags = { "Book Management" },
            responses = {
                    @ApiResponse(responseCode = "201", description = "Book created successfully",
                            content = @Content(schema = @Schema(implementation = GetBookDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            }
    )    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = {"multipart/form-data"})
    public GetBookDTO createBook(@AuthenticationPrincipal UserDetails userDetails,
                                 @RequestPart("book")
                                 @Parameter(description = "Book metadata", required = true)
                                 AddBookDTO dto,

                                 @RequestPart("bookContent")
                                     @Parameter(description = "PDF file content", content = @Content(mediaType = MediaType.APPLICATION_PDF_VALUE))
                                     MultipartFile bookContentFile,
                                 @RequestPart("bookCover")
                                     @Parameter(description = "Cover image (PNG/JPEG)", content = @Content(mediaType = MediaType.IMAGE_PNG_VALUE))
                                     MultipartFile bookCoverFile) {
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

    @Operation(
            summary = "Download book content",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Book file content",
                            content = @Content(schema = @Schema(type = "string", format = "binary"))),
                    @ApiResponse(responseCode = "404", description = "Book not found")
            }
    )
    @GetMapping("/{bookId}/download")
    public ResponseEntity<Resource> downloadBook(@PathVariable Long bookId) {
        Resource resource = bookService.getBookContent(bookId);
        BookContent bookContent = bookService.getBookContentDetails(bookId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(bookContent.getMimeType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + bookContent.getFileName() + "\"")
                .body(resource);
    }

    @Operation(security = {@SecurityRequirement(name = SwaggerConfig.BASIC_AUTH_SECURITY_SCHEME)})
    @PutMapping("/{id}")
    public ResponseEntity<GetBookDTO> updateBook(@AuthenticationPrincipal UserDetails userDetails,
                                                 @PathVariable Long id,
                                                 @RequestPart("book") AddBookDTO dto,
                                                 @RequestPart(value = "bookContent", required = false) MultipartFile bookContentFile,
                                                 @RequestPart(value = "bookCover", required = false) MultipartFile bookCoverFile) throws IOException {
        User user = userService.findByUsername(userDetails.getUsername());
        Book updatedBook = bookService.updateBook(user, id, dto, bookContentFile, bookCoverFile);
        return ResponseEntity.ok(bookMapper.toBookDTO(updatedBook));
    }
}
