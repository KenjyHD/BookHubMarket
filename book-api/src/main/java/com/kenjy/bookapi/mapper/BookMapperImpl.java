package com.kenjy.bookapi.mapper;

import com.kenjy.bookapi.entities.Books;
import com.kenjy.bookapi.dto.GetBookDTO;
import com.kenjy.bookapi.dto.AddBookDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BookMapperImpl implements BookMapper {
    private final String uploadDir = System.getProperty("user.dir") + "\\book-api\\src\\main\\resources\\file-storage\\";

    @Override
    public Books toBook(AddBookDTO dto, MultipartFile pfdFile) {
        if (dto == null) {
            return null;
        }

        Books book = new Books();
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setPrice(dto.getPrice());
        book.setGenre(dto.getGenre());
        book.setDescription(dto.getDescription());
        book.setBookPdfPath(uploadDir + pfdFile.getOriginalFilename());

        return book;
    }

    @Override
    public GetBookDTO toBookDTO(Books book) {
        if (book == null) {
            return null;
        }
        return new GetBookDTO(book.getId(), book.getTitle(), book.getAuthor(), book.getPrice(), book.getGenre(), book.getDescription(), book.getBookPdfPath());
    }
}
