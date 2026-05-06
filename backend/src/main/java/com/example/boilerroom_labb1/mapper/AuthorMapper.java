package com.example.boilerroom_labb1.mapper;

import com.example.boilerroom_labb1.dto.author.AuthorRequestDto;
import com.example.boilerroom_labb1.dto.author.AuthorResponseDto;
import com.example.boilerroom_labb1.entity.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {


    private final BookMapper bookMapper;

    public AuthorMapper(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    public AuthorResponseDto toResponseDto(Author author){
        return new AuthorResponseDto(
                author.getId(),
                author.getName()
        );
    }



    public Author toEntity(AuthorRequestDto request){
        return new Author(request.name());
    }
}
