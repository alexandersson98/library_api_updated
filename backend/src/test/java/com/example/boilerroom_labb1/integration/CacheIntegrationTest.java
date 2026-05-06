package com.example.boilerroom_labb1.integration;

import com.example.boilerroom_labb1.dto.author.AuthorRequestDto;
import com.example.boilerroom_labb1.dto.author.AuthorResponseDto;
import com.example.boilerroom_labb1.dto.book.BookRequestDto;
import com.example.boilerroom_labb1.dto.book.v1.BookResponseDto;
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
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CacheIntegrationTest {

    @Autowired private TestRestTemplate restTemplate;
    @Autowired private CacheManager cacheManager;
    @Autowired private AuthorRepository authorRepository;
    @Autowired private BookRepository bookRepository;
    @Autowired private LoanRepository loanRepository;
    @Autowired private LoanHistoryRepository loanHistoryRepository;

    @BeforeEach
    void setUp() {
        loanHistoryRepository.deleteAll();
        loanRepository.deleteAll();
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        cacheManager.getCacheNames().forEach(name -> cacheManager.getCache(name).clear());
    }

    private Long createAuthorAndGetId() {
        ResponseEntity<AuthorResponseDto> response = restTemplate.postForEntity(
                "/api/v1/author", new AuthorRequestDto("Test Author"), AuthorResponseDto.class);
        return response.getBody().id();
    }

    private Long createBookAndGetId(Long authorId) {
        ResponseEntity<BookResponseDto> response = restTemplate.postForEntity(
                "/api/v1/books", new BookRequestDto("Test Book", authorId, "1234", 2000), BookResponseDto.class);
        return response.getBody().id();
    }

    @Test
    void shouldPopulateBookCacheAfterGetById() {
        Long authorId = createAuthorAndGetId();
        Long bookId = createBookAndGetId(authorId);

        restTemplate.getForEntity("/api/v1/books/" + bookId, BookResponseDto.class);

        Cache bookCache = cacheManager.getCache("book");
        assertThat(bookCache.get(bookId)).isNotNull();
    }

    @Test
    void shouldEvictBookCacheAfterCreatingBook() {
        Long authorId = createAuthorAndGetId();
        Long bookId = createBookAndGetId(authorId);

        restTemplate.getForEntity("/api/v1/books/" + bookId, BookResponseDto.class);
        assertThat(cacheManager.getCache("book").get(bookId)).isNotNull();

        createBookAndGetId(authorId);

        assertThat(cacheManager.getCache("book").get(bookId)).isNull();
    }

    @Test
    void shouldEvictLoanCacheAfterCreatingLoan() {
        Long authorId = createAuthorAndGetId();
        Long bookId1 = createBookAndGetId(authorId);
        Long bookId2 = createBookAndGetId(authorId);

        restTemplate.postForEntity("/api/v1/loans", new LoanRequestDto(bookId1), LoanResponseDto.class);
        restTemplate.getForEntity("/api/v1/loans", Object.class);

        Cache loanCache = cacheManager.getCache("loan");

        restTemplate.postForEntity("/api/v1/loans", new LoanRequestDto(bookId2), LoanResponseDto.class);

        assertThat(((java.util.Map<?, ?>) loanCache.getNativeCache())).isEmpty();
    }
}
