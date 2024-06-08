package com.kenjy.bookapi.mapper;

import com.kenjy.bookapi.dto.GetAuthorRequestDTO;
import com.kenjy.bookapi.entities.AuthorRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorRequestMapper {
    public GetAuthorRequestDTO toGetAuthorRequestDTO(AuthorRequest ar) {
        return new GetAuthorRequestDTO(ar.getId(), ar.getBook().getId(), ar.getUser().getUsername(), ar.getBook().getTitle(), ar.getRequestDate(), ar.getStatus());
    }
}
