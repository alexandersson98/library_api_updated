package com.example.boilerroom_labb1.controller;


import com.example.boilerroom_labb1.dto.book.v1.EditBookRequestDto;
import com.example.boilerroom_labb1.dto.book.BookRequestDto;
import com.example.boilerroom_labb1.dto.book.v1.BookResponseDto;
import com.example.boilerroom_labb1.openapi.BadRequestResponse;
import com.example.boilerroom_labb1.openapi.NotFoundResponse;
import com.example.boilerroom_labb1.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService service;


    public BookController(BookService service){
        this.service = service;
    }

    @Operation(summary = "Create book",
            description = "Add a book to the database")
            @ApiResponse(responseCode = "201", description = "Created")
    @BadRequestResponse
    @PostMapping
    public ResponseEntity<BookResponseDto> create(@Valid @RequestBody BookRequestDto request) {
        BookResponseDto response = service.createBook(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(summary = "Get a book with id",
            description = "Returns a book with given id")

    @ApiResponse(responseCode = "200", description = "Found")
    @NotFoundResponse

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDto> getBookById(@PathVariable Long id){
        BookResponseDto response = service.getBookById(id);
        return ResponseEntity
                .ok()
                .body(response);
    }
    @Operation(summary = "Get all books",
            description = "Returns a list of all books")
            @ApiResponse(responseCode = "200", description = "Success")
    @GetMapping
    public ResponseEntity <Page<BookResponseDto>> getBooks(@ParameterObject Pageable pageable){
        return ResponseEntity.ok(service.getAll(pageable));
    }



    @Operation(summary = "Edit book",
            description = "Edit a book")
            @ApiResponse(responseCode = "200", description = "Book successfully updated")
    @NotFoundResponse
    @PatchMapping("/edit/{id}")
    public ResponseEntity<BookResponseDto>editBook(@PathVariable Long id, @RequestBody EditBookRequestDto editBookRequestDto){
        BookResponseDto response = service.editBook(id, editBookRequestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }
}
