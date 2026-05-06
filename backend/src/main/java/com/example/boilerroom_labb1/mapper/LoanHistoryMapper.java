package com.example.boilerroom_labb1.mapper;

import com.example.boilerroom_labb1.dto.loan.LoanHistoryResponseDto;
import com.example.boilerroom_labb1.entity.Loan;
import com.example.boilerroom_labb1.entity.LoanHistory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class LoanHistoryMapper {

    public LoanHistory toEntity(Loan loan) {
        LoanHistory history = new LoanHistory();
        history.setId(loan.getId());
        history.setBook(loan.getBook());
        history.setLoanDate(loan.getLoanDate());
        history.setReturnDate(LocalDate.now());
        return history;
    }
    public LoanHistoryResponseDto toResponseDto(LoanHistory history) {
        return new LoanHistoryResponseDto(
                history.getId(),
                history.getBook().getTitle(),
                history.getLoanDate(),
                history.getReturnDate(),
                "Book has been returned"
        );
    }
}
