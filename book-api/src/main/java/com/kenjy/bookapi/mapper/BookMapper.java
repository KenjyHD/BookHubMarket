package com.kenjy.bookapi.mapper;

import com.kenjy.bookapi.entities.Books;
import com.kenjy.bookapi.dto.GetBookDTO;
import com.kenjy.bookapi.dto.AddBookDTO;
import org.springframework.web.multipart.MultipartFile;

public interface BookMapper {

    Books toBook(AddBookDTO addBookDTO, MultipartFile pdfFile);

    GetBookDTO toBookDTO(Books book);
}