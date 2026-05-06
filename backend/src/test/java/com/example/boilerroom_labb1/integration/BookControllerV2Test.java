package com.example.boilerroom_labb1.integration;

import com.example.boilerroom_labb1.dto.author.AuthorRequestDto;
import com.example.boilerroom_labb1.dto.author.AuthorResponseDto;
import com.example.boilerroom_labb1.dto.book.BookRequestDto;
import com.example.boilerroom_labb1.dto.book.v2.BookResponseDtoV2;
import com.example.boilerroom_labb1.dto.book.v2.BookWrapperDtoV2;
import com.example.boilerroom_labb1.dto.book.v2.BookWrapperGetByIdDtoV2;
import com.example.boilerroom_labb1.repository.AuthorRepository;
import com.example.boilerroom_labb1.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class BookControllerV2Test {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;


    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();
    }

    @Test
    void shouldCreateBookAndReturnV2Response() {
        AuthorRequestDto authorRequest = new AuthorRequestDto("Matt Duffer");
        ResponseEntity<AuthorResponseDto> authorResponse =
                restTemplate.postForEntity("/api/v1/author",
                        authorRequest,
                        AuthorResponseDto.class);

        Long authorId = authorResponse.getBody().id();
        String authorName = authorResponse.getBody().name();

        BookRequestDto request = new BookRequestDto(
                "Stranger Things",
                authorId,
                "EV443-FRed",
                2016
        );

        ResponseEntity<BookResponseDtoV2> response = restTemplate.postForEntity("/api/v2/books",
                request,
                BookResponseDtoV2.class);
        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().title()).isEqualTo("Stranger Things");
        assertThat(response.getBody().author().name()).isEqualTo(authorName);
        assertThat(response.getBody().author().id()).isEqualTo(authorId);
        assertThat(response.getBody().isbn()).isEqualTo("EV443-FRed");
        assertThat(response.getBody().publishedYear()).isEqualTo(2016);
    }

    @Test
    void shouldReturnV2BookResponseWhenBookExists() {
        AuthorRequestDto authorRequest = new AuthorRequestDto("Matt Duffer");
        ResponseEntity<AuthorResponseDto>authorResponse =
                restTemplate.postForEntity("/api/v1/author",
                        authorRequest,
                        AuthorResponseDto.class);

        Long authorId = authorResponse.getBody().id();



        BookRequestDto request = new BookRequestDto(
                "Stranger Things",
                authorId,
                "EV443-FRed",
                2016
        );

        ResponseEntity<BookResponseDtoV2> created = restTemplate.postForEntity("/api/v2/books",
                request,
                BookResponseDtoV2.class);

        Long bookId = created.getBody().id();

        ResponseEntity<BookWrapperGetByIdDtoV2> response = restTemplate.getForEntity("/api/v2/books/" + bookId,
                BookWrapperGetByIdDtoV2.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().version()).isEqualTo("V2");



        BookResponseDtoV2 book = response.getBody().data();

        assertThat(book.title()).isEqualTo("Stranger Things");
        assertThat(book.author().id()).isEqualTo(authorId);
    }



}