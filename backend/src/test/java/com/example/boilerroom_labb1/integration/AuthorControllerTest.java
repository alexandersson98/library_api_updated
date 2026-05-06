package com.example.boilerroom_labb1.integration;


import com.example.boilerroom_labb1.dto.author.AuthorRequestDto;
import com.example.boilerroom_labb1.dto.author.AuthorResponseDto;
import com.example.boilerroom_labb1.dto.book.BookRequestDto;
import com.example.boilerroom_labb1.dto.book.v1.BookResponseDto;
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
public class AuthorControllerTest {


    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    LoanRepository loanRepository;


    @BeforeEach
    void setUp() {
        loanRepository.deleteAll();
        bookRepository.deleteAll();
        authorRepository.deleteAll();
    }



    @Test
    void shouldCreateAuthor(){
        AuthorRequestDto authorRequest = new AuthorRequestDto("Herbert Bengtsson");
        ResponseEntity<AuthorResponseDto>authorResponse = restTemplate.postForEntity("/api/v1/author",
                authorRequest,
                AuthorResponseDto.class);


        assertEquals(HttpStatus.CREATED, authorResponse.getStatusCode());
        AuthorResponseDto body = authorResponse.getBody();
        assertNotNull(body);
        assertNotNull(body.id());
        assertEquals("Herbert Bengtsson", body.name());
    }

    @Test
    void shouldReturnAuthorWithId(){
        AuthorRequestDto authorRequest = new AuthorRequestDto("Herbert Bengtsson");
        ResponseEntity<AuthorResponseDto>authorResponse = restTemplate.postForEntity("/api/v1/author",
                authorRequest,
                AuthorResponseDto.class);

        Long authorId = authorResponse.getBody().id();
        ResponseEntity<AuthorResponseDto>getById = restTemplate.getForEntity("/api/v1/author/{id}",
                AuthorResponseDto.class,
                authorId);

        assertEquals(HttpStatus.OK, getById.getStatusCode());
        AuthorResponseDto fetchedBody = getById.getBody();
        assertNotNull(fetchedBody);
        assertEquals("Herbert Bengtsson",fetchedBody.name());
        assertEquals(authorResponse.getBody().id(), fetchedBody.id());
    }


    @Test
    void shouldReturnBooksByAuthorsId(){
        AuthorRequestDto authorRequest = new AuthorRequestDto("Herbert Bengtsson");
        ResponseEntity<AuthorResponseDto>authorResponse = restTemplate.postForEntity("/api/v1/author",
                authorRequest,
                AuthorResponseDto.class);

        Long authorId = authorResponse.getBody().id();

        BookRequestDto bookRequest = new BookRequestDto("Harry Potter", authorResponse.getBody().id(), "eeee", 2007);
        ResponseEntity<BookResponseDto> bookResponse = restTemplate.postForEntity("/api/v1/books",
                bookRequest,
                BookResponseDto.class);

        ResponseEntity<Map> booksByAuthor = restTemplate.getForEntity(
                "/api/v1/author/{authorId}/books",
                Map.class,
                authorId
        );
        assertEquals(HttpStatus.CREATED, authorResponse.getStatusCode());
        assertEquals(HttpStatus.OK, booksByAuthor.getStatusCode());
        assertNotNull(booksByAuthor.getBody());

        List<Map> content = (List<Map>) booksByAuthor.getBody().get("content");
        assertEquals(1, content.size());
        assertEquals("Harry Potter", content.get(0).get("title"));
    }

    @Test
    void shouldReturn404WhenNotFound(){
        Long id = 1L;
        ResponseEntity<String>response = restTemplate.getForEntity("/api/v1/author/{id}",
              String.class,
                id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
