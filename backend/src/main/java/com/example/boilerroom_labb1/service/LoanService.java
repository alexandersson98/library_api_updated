package com.example.boilerroom_labb1.service;


import com.example.boilerroom_labb1.dto.loan.LoanHistoryResponseDto;
import com.example.boilerroom_labb1.dto.loan.LoanRequestDto;
import com.example.boilerroom_labb1.dto.loan.LoanResponseDto;
import com.example.boilerroom_labb1.entity.Book;
import com.example.boilerroom_labb1.entity.Loan;
import com.example.boilerroom_labb1.entity.LoanHistory;
import com.example.boilerroom_labb1.exceptions.BookAlreadyLoanedException;
import com.example.boilerroom_labb1.exceptions.NotFoundWithIdException;
import com.example.boilerroom_labb1.exceptions.NotFoundException;
import com.example.boilerroom_labb1.exceptions.ValidationException;
import com.example.boilerroom_labb1.mapper.LoanHistoryMapper;
import com.example.boilerroom_labb1.mapper.LoanMapper;
import com.example.boilerroom_labb1.repository.BookRepository;
import com.example.boilerroom_labb1.repository.LoanHistoryRepository;
import com.example.boilerroom_labb1.repository.LoanRepository;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoanService {
    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final LoanMapper loanMapper;
    private final LoanHistoryRepository loanHistory;
    private final LoanHistoryMapper loanHistoryMapper;


    public LoanService(LoanRepository loanRepository,
                       BookRepository bookRepository,
                       LoanMapper loanMapper, LoanHistoryRepository loanHistory, LoanHistoryMapper loanHistoryMapper) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.loanMapper = loanMapper;
        this.loanHistory = loanHistory;
        this.loanHistoryMapper = loanHistoryMapper;
    }
    @Caching(evict = {
            @CacheEvict(value = "loan", allEntries = true),
            @CacheEvict(value = "book", allEntries = true)
    })
    @Transactional
    public LoanResponseDto createLoan(LoanRequestDto request){
        Book book = bookRepository.findByIdWithLock(request.bookId())
                .orElseThrow(() -> new NotFoundException("Book not found"));
        if (loanRepository.existsByBookId(book.getId())) {
            throw new BookAlreadyLoanedException("Book already loaned");
        }

        try {

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setLoanDate(LocalDate.now());

        Loan savedLoan = loanRepository.saveAndFlush(loan);

        return loanMapper.toResponseDto(savedLoan);
    } catch (DataIntegrityViolationException e){
        throw new BookAlreadyLoanedException("Book already loaned");}
    }

    @Cacheable("loan")
    public Page<LoanResponseDto> getAllActiveLoans(Pageable pageable){
           return loanRepository.findAll(pageable)
                .map(loanMapper::toResponseDto);

    }

    @Caching(evict = {
            @CacheEvict(value = "loan", allEntries = true),
            @CacheEvict(value = "book", allEntries = true),
            @CacheEvict(value = "loanHistory", allEntries = true),
    })
    @Transactional
    public LoanHistoryResponseDto returnBook(Long id){
        if(loanHistory.existsById(id)){
            throw new ValidationException("Book already returned");
        }
        Loan loan = loanRepository.findById(id).orElseThrow(()-> new NotFoundWithIdException("Loan not found with id: ", id));
        LoanHistory response = loanHistory.save(loanHistoryMapper.toEntity(loan));
        loanRepository.deleteById(id);
         return loanHistoryMapper.toResponseDto(response);

    }


    @Cacheable("loanHistory")
    public Page<LoanHistoryResponseDto>getLoanHistoryList(Pageable pageable){
        return loanHistory.findAll(pageable)
                .map(loanHistoryMapper:: toResponseDto);
    }
}
