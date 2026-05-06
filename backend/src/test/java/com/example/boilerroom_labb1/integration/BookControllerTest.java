package com.example.boilerroom_labb1.integration;


import com.example.boilerroom_labb1.dto.author.AuthorRequestDto;
import com.example.boilerroom_labb1.dto.author.AuthorResponseDto;
import com.example.boilerroom_labb1.dto.book.BookRequestDto;
import com.example.boilerroom_labb1.dto.book.v1.BookResponseDto;
import com.example.boilerroom_labb1.dto.book.v1.EditBookRequestDto;
import com.example.boilerroom_labb1.exceptions.handler.ApiErrorResponse;
import com.example.boilerroom_labb1.repository.AuthorRepository;
import com.example.boilerroom_labb1.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class BookControllerTest {
@Autowired private TestRestTemplate restTemplate;
@Autowired private BookRepository bookRepository;
@Autowired private AuthorRepository authorRepository;


@BeforeEach void setUp(){
    bookRepository.deleteAll();
    authorRepository.deleteAll();
}


    @Test
    void shouldReturn201AndSaveBookWhenCreatingValidBook(){
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

            ResponseEntity<BookResponseDto> response = restTemplate.postForEntity("/api/v1/books",
                    request,
                    BookResponseDto.class);
            assertThat(response.getStatusCode().value()).isEqualTo(201);
            assertThat(response.getBody()).isNotNull();

            assertThat(response.getBody().title()).isEqualTo("Stranger Things");
            assertThat(response.getBody().authorName()).isEqualTo("Matt Duffer");
            assertThat(response.getBody().isbn()).isEqualTo("EV443-FRed");
            assertThat(response.getBody().publishedYear()).isEqualTo(2016);
        }

    @Test
    void shouldReturn200AndBookWhenBookExists() {
        AuthorRequestDto authorRequest = new AuthorRequestDto("Ostsson Bengt");
        ResponseEntity<AuthorResponseDto>authorResponse =
                restTemplate.postForEntity("/api/v1/author",
                        authorRequest,
                        AuthorResponseDto.class);

        Long authorId = authorResponse.getBody().id();



        BookRequestDto request = new BookRequestDto(
                "Flammande Osten",
                authorId,
                "EV443-FRed",
                2016
        );

        ResponseEntity<BookResponseDto> response = restTemplate.postForEntity("/api/v1/books",
                request,
                BookResponseDto.class);

        Long bookId = response.getBody().id();

        ResponseEntity<BookResponseDto> getBookWithId = restTemplate.getForEntity("/api/v1/books/{id}",
                BookResponseDto.class,
                bookId
                );

        assertThat(getBookWithId.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getBookWithId).isNotNull();

        assertThat(getBookWithId.getBody().title()).isEqualTo("Flammande Osten");
        assertThat(getBookWithId.getBody().authorName()).isEqualTo("Ostsson Bengt");
    }

    @Test
    void shouldThrowValidationException(){
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
                912
        );

        ResponseEntity<ApiErrorResponse> response = restTemplate.postForEntity("/api/v1/books",
                request,
                ApiErrorResponse.class);

       assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
       assertThat(response.getBody()).isNotNull();
       assertThat(response.getBody().message().contains("Published year must be greater than 1700"));

    }

    @Test
    void shouldReturn200AndUpdatedBookWhenEditingBook() {
        AuthorRequestDto authorRequest = new AuthorRequestDto("Matt Duffer");
        ResponseEntity<AuthorResponseDto> authorResponse =
                restTemplate.postForEntity("/api/v1/author", authorRequest, AuthorResponseDto.class);

        AuthorRequestDto newAuthorRequest = new AuthorRequestDto("JK Rowling");
        ResponseEntity<AuthorResponseDto> newAuthorResponse =
                restTemplate.postForEntity("/api/v1/author", newAuthorRequest, AuthorResponseDto.class);
        Long newAuthorId = newAuthorResponse.getBody().id();

        Long authorId = authorResponse.getBody().id();

        BookRequestDto bookRequest = new BookRequestDto("Stranger Things", authorId, "EV443-FRed", 2016);
        ResponseEntity<BookResponseDto> bookResponse =
                restTemplate.postForEntity("/api/v1/books", bookRequest, BookResponseDto.class);

        Long bookId = bookResponse.getBody().id();

        EditBookRequestDto editRequest = new EditBookRequestDto("Stranger Things 2", newAuthorId , "ISBN", 2022);

        ResponseEntity<BookResponseDto> editResponse = restTemplate.exchange(
                "/api/v1/books/edit/" + bookId,
                HttpMethod.PATCH,
                new HttpEntity<>(editRequest),
                BookResponseDto.class
        );

        assertThat(editResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(editResponse.getBody()).isNotNull();
        assertThat(editResponse.getBody().title()).isEqualTo("Stranger Things 2");
        assertThat(editResponse.getBody().authorName()).isEqualTo("JK Rowling");
        assertThat(editResponse.getBody().isbn()).isEqualTo("ISBN");
        assertThat(editResponse.getBody().publishedYear()).isEqualTo(2022);
    }
}


