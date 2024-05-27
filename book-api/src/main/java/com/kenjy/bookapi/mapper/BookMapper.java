package com.kenjy.bookapi.mapper;

import com.kenjy.bookapi.entities.Book;
import com.kenjy.bookapi.dto.GetBookDTO;
import com.kenjy.bookapi.dto.AddBookDTO;
import org.springframework.web.multipart.MultipartFile;

public interface BookMapper {

    Book toBook(AddBookDTO addBookDTO, MultipartFile pdfFile);

    GetBookDTO toBookDTO(Book book);
}