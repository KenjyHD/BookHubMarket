package com.kenjy.bookapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Book creation DTO")
public class AddBookDTO {

    @NotBlank(message = "Title is required")
    @Schema(description = "Book title", example = "War and Peace")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    @NotBlank(message = "Price is required")
    private Float price;

    private String genre;

    private String description;
}