package com.kenjy.bookapi.mapper;

import com.kenjy.bookapi.dto.GetAuthorRequestDTO;
import com.kenjy.bookapi.entities.AuthorRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorRequestMapper {
    public GetAuthorRequestDTO toGetAuthorRequestDTO(AuthorRequest ar) {
        Long bookId = ar.getBook() != null ? ar.getBook().getId() : null;
        String bookTitle = ar.getBook() != null ? ar.getBook().getTitle() : null;
        return new GetAuthorRequestDTO(ar.getId(), bookId, ar.getUser().getUsername(), bookTitle, ar.getRequestDate(), ar.getStatus());
    }
}
