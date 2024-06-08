package com.kenjy.bookapi.dto;

import com.kenjy.bookapi.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAuthorRequestDTO {
    private Long id;
    private Long bookId;
    private String username;
    private String title;
    private LocalDateTime requestDate;
    private RequestStatus status;
}
