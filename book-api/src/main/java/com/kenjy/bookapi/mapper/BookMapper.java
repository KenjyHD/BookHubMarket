package com.kenjy.bookapi.mapper;

import com.kenjy.bookapi.entities.Book;
import com.kenjy.bookapi.dto.GetBookDTO;
import com.kenjy.bookapi.dto.AddBookDTO;
import com.kenjy.bookapi.entities.BookContent;
import com.kenjy.bookapi.entities.BookCover;
import com.kenjy.bookapi.entities.User;
import com.kenjy.bookapi.repository.BookContentRepository;
import com.kenjy.bookapi.repository.BookCoverRepository;
import com.kenjy.bookapi.security.WebSecurityConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookMapper {
    private final BookContentRepository bookContentRepository;
    private final BookCoverRepository bookCoverRepository;

    public Book toBook(User user, AddBookDTO dto, MultipartFile bookContentFile, MultipartFile bookCoverFile) {
        if (dto == null) {
            return null;
        }

        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setAuthorName(dto.getAuthor());
        book.setPrice(dto.getPrice());
        book.setGenre(dto.getGenre());
        book.setDescription(dto.getDescription());
        book.setAuthor(user);
        if (user.getRole().equals(WebSecurityConfig.USER)) {
            book.setAuthor(null);
        }

        BookContent bookContent = new BookContent();
        bookContent.setFileName(bookContentFile.getOriginalFilename());
        bookContent.setMimeType(bookContentFile.getContentType());
        BookContent savedBookContent = bookContentRepository.save(bookContent);
        book.setBookContent(savedBookContent);

        BookCover bookCover = new BookCover();
        bookCover.setFileName(bookCoverFile.getOriginalFilename());
        bookCover.setMimeType(bookCoverFile.getContentType());
        BookCover savedBookCover = bookCoverRepository.save(bookCover);
        book.setBookCover(savedBookCover);

        return book;
    }

    public GetBookDTO toBookDTO(Book book) {
        if (book == null) {
            return null;
        }
        Long authorId = book.getAuthor() != null ? book.getAuthor().getId() : null;
        UUID bookCoverId = book.getBookCover() != null ? book.getBookCover().getId() : null;
        return new GetBookDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthorName(),
                book.getPrice(),
                book.getGenre(),
                book.getDescription(),
                authorId,
                bookCoverId);
    }
}
