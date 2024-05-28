package com.kenjy.bookapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseRequestPostDTO {
    private Long userId;
    private Long bookId;
}
