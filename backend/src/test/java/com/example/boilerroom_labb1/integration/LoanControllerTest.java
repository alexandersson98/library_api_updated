package com.example.boilerroom_labb1.integration;


import com.example.boilerroom_labb1.dto.author.AuthorRequestDto;
import com.example.boilerroom_labb1.dto.author.AuthorResponseDto;
import com.example.boilerroom_labb1.dto.book.BookRequestDto;
import com.example.boilerroom_labb1.dto.book.v1.BookResponseDto;
import com.example.boilerroom_labb1.dto.loan.LoanHistoryResponseDto;
import com.example.boilerroom_labb1.dto.loan.LoanRequestDto;
import com.example.boilerroom_labb1.dto.loan.LoanResponseDto;
import com.example.boilerroom_labb1.repository.AuthorRepository;
import com.example.boilerroom_labb1.repository.BookRepository;
import com.example.boilerroom_labb1.repository.LoanHistoryRepository;
import com.example.boilerroom_labb1.repository.LoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class LoanControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    LoanHistoryRepository loanHistoryRepository;

    @BeforeEach
    void setUp() {
        loanHistoryRepository.deleteAll();
        loanRepository.deleteAll();
        bookRepository.deleteAll();
        authorRepository.deleteAll();
    }

    @Test
    void shouldCreateLoan() {
        AuthorRequestDto authorRequest = new AuthorRequestDto("Joel Göransson");

        ResponseEntity<AuthorResponseDto> authorResponse = restTemplate.postForEntity("/api/v1/author",
                authorRequest,
                AuthorResponseDto.class);

        Long authorId = authorResponse.getBody().id();

        BookRequestDto bookRequest = new BookRequestDto("Peaky Blinders", authorId, "eeee", 2006);
        ResponseEntity<BookResponseDto> bookResponse = restTemplate.postForEntity("/api/v1/books",
                bookRequest,
                BookResponseDto.class);

        Long bookId = bookResponse.getBody().id();

        LoanRequestDto loanRequest = new LoanRequestDto(bookId);
        ResponseEntity<LoanResponseDto> loanResponse = restTemplate.postForEntity("/api/v1/loans",
                loanRequest,
                LoanResponseDto.class);

        assertEquals(HttpStatus.CREATED, loanResponse.getStatusCode());
        assertNotNull(loanResponse.getBody());
        assertNotNull(loanResponse.getBody().id());
    }

    @Test
    void shouldReturnAllLoans() {
        AuthorRequestDto authorRequest = new AuthorRequestDto("Joel Göransson");

        ResponseEntity<AuthorResponseDto> authorResponse = restTemplate.postForEntity("/api/v1/author",
                authorRequest,
                AuthorResponseDto.class);

        Long authorId = authorResponse.getBody().id();

        BookRequestDto bookRequest = new BookRequestDto("Peaky Blinders", authorId, "eeee", 2006);
        ResponseEntity<BookResponseDto> bookResponse = restTemplate.postForEntity("/api/v1/books",
                bookRequest,
                BookResponseDto.class);

        Long bookId = bookResponse.getBody().id();

        LoanRequestDto loanRequest = new LoanRequestDto(bookId);
        ResponseEntity<LoanResponseDto> loanResponse = restTemplate.postForEntity("/api/v1/loans",
                loanRequest,
                LoanResponseDto.class);


        ResponseEntity<Map> activeLoans = restTemplate.getForEntity("/api/v1/loans", Map.class);

        assertEquals(HttpStatus.CREATED, loanResponse.getStatusCode());
        assertEquals(HttpStatus.OK, activeLoans.getStatusCode());

        List<Map> content = (List<Map>) activeLoans.getBody().get("content");
        assertNotNull(content);
        assertTrue(content.size() > 0);
        assertNotNull(content.get(0));
    }

    @Test
    void shouldReturn404WhenCreateLoanAndBookNotFound() {
        Long bookId = 999L;
        LoanRequestDto loanRequest = new LoanRequestDto(bookId);
        ResponseEntity<String> response = restTemplate.postForEntity("/api/v1/loans",
                loanRequest,
                String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldReturnBadRequestWhenTryingToLoanSameBookTwice(){
        AuthorRequestDto authorRequest = new AuthorRequestDto("Joel Göransson");

        ResponseEntity<AuthorResponseDto> authorResponse = restTemplate.postForEntity("/api/v1/author",
                authorRequest,
                AuthorResponseDto.class);

        Long authorId = authorResponse.getBody().id();

        BookRequestDto bookRequest = new BookRequestDto("Peaky Blinders", authorId, "eeee", 2006);
        ResponseEntity<BookResponseDto> bookResponse = restTemplate.postForEntity("/api/v1/books",
                bookRequest,
                BookResponseDto.class);

        Long bookId = bookResponse.getBody().id();

        LoanRequestDto loanRequest = new LoanRequestDto(bookId);
        ResponseEntity<LoanResponseDto> loanResponse = restTemplate.postForEntity("/api/v1/loans",
                loanRequest,
                LoanResponseDto.class);


        ResponseEntity<LoanResponseDto> loanResponse2 = restTemplate.postForEntity("/api/v1/loans",
                loanRequest,
                LoanResponseDto.class);

        assertEquals(HttpStatus.CREATED, loanResponse.getStatusCode());
        assertEquals(HttpStatus.CONFLICT, loanResponse2.getStatusCode());
    }

    @Test
    void shouldAllowOnlyOneLoanWhenConcurrentRequests()throws Exception{
        AuthorRequestDto authorRequest = new AuthorRequestDto("Joel Göransson");

        ResponseEntity<AuthorResponseDto> authorResponse = restTemplate.postForEntity("/api/v1/author",
                authorRequest,
                AuthorResponseDto.class);

        Long authorId = authorResponse.getBody().id();

        BookRequestDto bookRequest = new BookRequestDto("Peaky Blinders", authorId, "eeee", 2006);
        ResponseEntity<BookResponseDto> bookResponse = restTemplate.postForEntity("/api/v1/books",
                bookRequest,
                BookResponseDto.class);

        Long bookId = bookResponse.getBody().id();

        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(2);

        Callable<ResponseEntity<LoanResponseDto>> task = () -> {
            try {
                startLatch.await();

            LoanRequestDto loanRequest = new LoanRequestDto(bookId);
            return restTemplate.postForEntity("/api/v1/loans",
                    loanRequest,
                    LoanResponseDto.class
            );
        }finally {
                doneLatch.countDown();
            }
        };

            Future<ResponseEntity<LoanResponseDto>> future1 = executor.submit(task);
            Future<ResponseEntity<LoanResponseDto>> future2 = executor.submit(task);
        startLatch.countDown();
        doneLatch.await();
        executor.shutdown();


       List<ResponseEntity<LoanResponseDto>>responses = Stream.of(future1, future2)
                       .map(f -> {
                           try { return f.get(); }
                           catch (Exception e) { throw new RuntimeException(e); }
                               })
                               .toList();

       assertThat(loanRepository.count()).isEqualTo(1);

       assertThat(responses.stream()
               .filter(f -> f.getStatusCode() == HttpStatus.CREATED)
               .count()).isEqualTo(1);

       assertThat(responses.stream()
               .filter(f -> f.getStatusCode() == HttpStatus.CONFLICT)
               .count()).isEqualTo(1);

        }

    @Test
    void shouldReturnBookAndRemoveFromActiveLoans(){
        AuthorRequestDto authorRequest = new AuthorRequestDto("Joel Göransson");

        ResponseEntity<AuthorResponseDto> authorResponse = restTemplate.postForEntity("/api/v1/author",
                authorRequest,
                AuthorResponseDto.class);

        Long authorId = authorResponse.getBody().id();

        BookRequestDto bookRequest = new BookRequestDto("Peaky Blinders", authorId, "eeee", 2006);
        ResponseEntity<BookResponseDto> bookResponse = restTemplate.postForEntity("/api/v1/books",
                bookRequest,
                BookResponseDto.class);

        Long bookId = bookResponse.getBody().id();

        LoanRequestDto loanRequest = new LoanRequestDto(bookId);
        ResponseEntity<LoanResponseDto> loanResponse = restTemplate.postForEntity("/api/v1/loans",
                loanRequest,
                LoanResponseDto.class);

        Long loanId = loanResponse.getBody().id();

        ResponseEntity<LoanHistoryResponseDto> returnResponse = restTemplate.exchange(
                "/api/v1/loans/" + loanId,
                HttpMethod.PATCH,
                null,
                LoanHistoryResponseDto.class);

        assertEquals(HttpStatus.OK, returnResponse.getStatusCode());
        assertNotNull(returnResponse.getBody());
        assertEquals("Book has been returned", returnResponse.getBody().message());
        assertThat(loanRepository.count()).isEqualTo(0);
    }
    @Test
    void shouldReturn400WhenBookAlreadyReturned(){
        AuthorRequestDto authorRequest = new AuthorRequestDto("Joel Göransson");

        ResponseEntity<AuthorResponseDto> authorResponse = restTemplate.postForEntity("/api/v1/author",
                authorRequest,
                AuthorResponseDto.class);

        Long authorId = authorResponse.getBody().id();

        BookRequestDto bookRequest = new BookRequestDto("Peaky Blinders", authorId, "eeee", 2006);
        ResponseEntity<BookResponseDto> bookResponse = restTemplate.postForEntity("/api/v1/books",
                bookRequest,
                BookResponseDto.class);

        Long bookId = bookResponse.getBody().id();

        LoanRequestDto loanRequest = new LoanRequestDto(bookId);
        ResponseEntity<LoanResponseDto> loanResponse = restTemplate.postForEntity("/api/v1/loans",
                loanRequest,
                LoanResponseDto.class);

        Long loanId = loanResponse.getBody().id();

        restTemplate.exchange("/api/v1/loans/" + loanId, HttpMethod.PATCH, null, LoanHistoryResponseDto.class);

        ResponseEntity<String> returnAgain = restTemplate.exchange(
                "/api/v1/loans/" + loanId,
                HttpMethod.PATCH,
                null,
                String.class);

        assertEquals(HttpStatus.BAD_REQUEST, returnAgain.getStatusCode());
    }

    @Test
    void demonstratesRaceConditionProblem() throws Exception {
        // utan locking kan 2 trådar läsa boken samtidigt och båda ser den som ledig
        // då skapas 2 lån på samma bok vilket är fel
        // med pessimistic locking låses raden i databasen så bara en tråd åt gången kan läsa
        // testet shouldAllowOnlyOneLoanWhenConcurrentRequests visar att lösningen fungerar
    }

    @Test
    void shouldAllowOnlyOneLoanOutOf100ConcurrentRequests() throws Exception {
        AuthorRequestDto authorRequest = new AuthorRequestDto("Joel Göransson");
        ResponseEntity<AuthorResponseDto> authorResponse = restTemplate.postForEntity("/api/v1/author",
                authorRequest, AuthorResponseDto.class);
        Long authorId = authorResponse.getBody().id();

        BookRequestDto bookRequest = new BookRequestDto("Peaky Blinders", authorId, "eeee", 2006);
        ResponseEntity<BookResponseDto> bookResponse = restTemplate.postForEntity("/api/v1/books",
                bookRequest, BookResponseDto.class);
        Long bookId = bookResponse.getBody().id();

        int threadCount = 100;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        List<Future<ResponseEntity<LoanResponseDto>>> futures = new java.util.ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            futures.add(executor.submit(() -> {
                try {
                    startLatch.await();
                    LoanRequestDto loanRequest = new LoanRequestDto(bookId);
                    return restTemplate.postForEntity("/api/v1/loans", loanRequest, LoanResponseDto.class);
                } finally {
                    doneLatch.countDown();
                }
            }
            ));
        }

        startLatch.countDown();
        doneLatch.await();
        executor.shutdown();

        List<ResponseEntity<LoanResponseDto>> responses = futures.stream()
                .map(f -> {
                    try { return f.get(); }
                    catch (Exception e) { throw new RuntimeException(e); }
                })
                .toList();

        assertThat(loanRepository.count()).isEqualTo(1);

        assertThat(responses.stream()
                .filter(r -> r.getStatusCode() == HttpStatus.CREATED)
                .count()).isEqualTo(1);

            assertThat(responses.stream()
                .filter(r -> r.getStatusCode() == HttpStatus.CONFLICT)
                .count()).isEqualTo(99);
    }
}
