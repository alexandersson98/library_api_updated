package com.example.boilerroom_labb1.mapper;


import com.example.boilerroom_labb1.dto.author.AuthorResponseDto;
import com.example.boilerroom_labb1.dto.book.BookRequestDto;
import com.example.boilerroom_labb1.dto.book.v1.BookResponseDto;
import com.example.boilerroom_labb1.dto.book.v2.BookResponseDtoV2;
import com.example.boilerroom_labb1.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {
    public BookResponseDto toResponseDto
            (Book book){
        return new BookResponseDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor().getName(),
                book.getIsbn(),
                book.getPublishedYear(),
        book.getVersion());

    }

    public Book toEntity(BookRequestDto request){
          Book book = new Book();
                book.setTitle(request.title());
                book.setIsbn(request.isbn());
                book.setPublishedYear(request.publishedYear());
                return book;
    }

    public BookResponseDtoV2 toResponseDtoV2(Book book, Boolean available){
        return new BookResponseDtoV2(
                book.getId(),
                book.getTitle(),
                new AuthorResponseDto(book.getAuthor().getId(),
                        book.getAuthor().getName()),
                book.getIsbn(),
                book.getPublishedYear(),
                available
                );
    }
}
