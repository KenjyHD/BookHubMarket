package com.kenjy.bookapi.dto;

import com.kenjy.bookapi.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorRequestDTO {
    private Long id;
    private Long userId;
    private Long bookId;
    private RequestStatus status;
}
