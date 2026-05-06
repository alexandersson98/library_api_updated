package com.example.boilerroom_labb1.integration;


import com.example.boilerroom_labb1.dto.author.AuthorRequestDto;
import com.example.boilerroom_labb1.dto.author.AuthorResponseDto;
import com.example.boilerroom_labb1.dto.book.BookRequestDto;
import com.example.boilerroom_labb1.dto.book.v1.BookResponseDto;
import com.example.boilerroom_labb1.dto.loan.LoanRequestDto;
import com.example.boilerroom_labb1.dto.loan.LoanResponseDto;
import com.example.boilerroom_labb1.repository.AuthorRepository;
import com.example.boilerroom_labb1.repository.BookRepository;
import com.example.boilerroom_labb1.repository.LoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AuthorBookTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private LoanRepository loanRepository;


    @BeforeEach
    void setUp() {
        loanRepository.deleteAll();
        bookRepository.deleteAll();
        authorRepository.deleteAll();
    }

    @Test
    void shouldCreateAuthorAndBook_andReturnBookViaAuthorEndpoint() {
        AuthorRequestDto authorRequest = new AuthorRequestDto("Gunnar Larsson");
        ResponseEntity<AuthorResponseDto> authorResponse =
                restTemplate.postForEntity("/api/v1/author",
                        authorRequest,
                        AuthorResponseDto.class);


        assertEquals(HttpStatus.CREATED, authorResponse.getStatusCode());
        assertNotNull(authorResponse.getBody());
        assertNotNull(authorResponse.getBody().id());
        assertEquals("Gunnar Larsson", authorResponse.getBody().name());
        Long authorId = authorResponse.getBody().id();


        BookRequestDto bookRequest = new BookRequestDto("The hunger games", authorId, "edee-333", 2005);
        ResponseEntity<BookResponseDto> bookResponse = restTemplate.postForEntity("/api/v1/books",
                bookRequest,
                BookResponseDto.class
        );

        assertEquals(HttpStatus.CREATED, bookResponse.getStatusCode());
        assertNotNull(bookResponse.getBody());
        assertEquals("The hunger games", bookResponse.getBody().title());
        assertEquals(authorId, authorResponse.getBody().id());
        assertEquals("edee-333", bookResponse.getBody().isbn());
        assertEquals("Gunnar Larsson", bookResponse.getBody().authorName());
        assertEquals(2005, bookResponse.getBody().publishedYear());
    }


    @Test
    void shouldCreateAuthorBookAndLoan_AndReturnCreatedLoan() {
        AuthorRequestDto authorRequest = new AuthorRequestDto("Johan Bengtsson");

        ResponseEntity<AuthorResponseDto> authorResponse = restTemplate.postForEntity("/api/v1/author",
                authorRequest,
                AuthorResponseDto.class);

        Long authorId = authorResponse.getBody().id();

        BookRequestDto bookRequest = new BookRequestDto("The hunger games", authorId, "eeee", 2005);
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

        Long createdLoanId = loanResponse.getBody().id();


        ResponseEntity<Map> loanResponseDto = restTemplate.getForEntity("/api/v1/loans", Map.class);

        assertEquals(HttpStatus.OK, loanResponseDto.getStatusCode());
        assertNotNull(loanResponseDto);

        List<Map> content = (List<Map>) loanResponseDto.getBody().get("content");
        assertNotNull(content);

        Boolean found = content.stream()
                .anyMatch(loan -> createdLoanId.equals(((Number) loan.get("id")).longValue()));

        assertTrue(found);
    }
}
