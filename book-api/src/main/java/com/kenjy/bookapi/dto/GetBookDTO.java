package com.kenjy.bookapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetBookDTO {
    private Long id;
    private String title;
    private String author;
    private Float price;
    private String genre;
    private String description;
    private Long authorId;
    private UUID coverId;
}