package com.example.boilerroom_labb1.dto.book.v2;


import com.example.boilerroom_labb1.dto.author.AuthorResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
@Schema(description = "Version 2 response object representing a book")
public record BookResponseDtoV2(

        @Schema(description = "Unique identifier of the book", example = "1")
        Long id,

        @Schema(description = "Title of the book", example = "Harry Potter")
        String title,

        @Schema(description = "Author of the book", example = "J.K. Rowling")
        AuthorResponseDto author,

        @Schema(description = "ISBN number of the book", example = "9780747532743")
        String isbn,

        @Schema(description = "Year the book was published", example = "1997")
        int publishedYear,

        @Schema(description = "Availability status of the book", example = "true")
        boolean available
) {}