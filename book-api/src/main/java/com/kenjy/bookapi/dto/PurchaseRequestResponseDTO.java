package com.kenjy.bookapi.dto;

import com.kenjy.bookapi.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseRequestResponseDTO {
    private Long id;
    private Long userId;
    private Long bookId;
    private RequestStatus status;
    private LocalDateTime requestDate;
}
