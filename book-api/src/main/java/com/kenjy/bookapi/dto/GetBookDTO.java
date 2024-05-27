package com.kenjy.bookapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private String pdfFilePath;
}