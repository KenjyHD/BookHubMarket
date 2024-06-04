package com.kenjy.bookapi.dto;

import com.kenjy.bookapi.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetPurchaseRequestDTO {
    private Long id;
    private String username;
    private String bookTitle;
    private RequestStatus status;
}
