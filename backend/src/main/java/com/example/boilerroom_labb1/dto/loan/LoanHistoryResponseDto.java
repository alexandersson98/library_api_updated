package com.example.boilerroom_labb1.dto.loan;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record LoanHistoryResponseDto(

        @Schema(description = "Unique identifier of the loan", example = "1")
        Long id,

        @Schema(description = "Title of the returned book", example = "Harry Potter")
        String bookTitle,

        @Schema(description = "Date the book was loaned", example = "2026-04-01")
        LocalDate loanDate,

        @Schema(description = "Date the book was returned", example = "2026-04-16")
        LocalDate returnDate,

        @Schema(description = "Status message", example = "Book has been returned")
        String message
) {}
