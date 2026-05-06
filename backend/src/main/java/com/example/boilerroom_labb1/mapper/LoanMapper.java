package com.example.boilerroom_labb1.mapper;


import com.example.boilerroom_labb1.dto.loan.LoanResponseDto;
import com.example.boilerroom_labb1.entity.Loan;
import org.springframework.stereotype.Component;

@Component
public class LoanMapper {

    public LoanResponseDto toResponseDto(Loan loan) {
        return new LoanResponseDto(
                loan.getId(),
                loan.getBook().getId(),
                loan.getBook().getTitle(),
                loan.getLoanDate()
        );
    }
}