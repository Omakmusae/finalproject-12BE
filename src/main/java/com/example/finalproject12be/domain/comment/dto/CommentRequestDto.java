package com.example.finalproject12be.domain.comment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class CommentRequestDto {

    @NotBlank
    @Size(max = 100)
    private String contents;

    private boolean isForeign;

    public CommentRequestDto(String contents, boolean isForeign) {
        this.contents = contents;
        this.isForeign = isForeign;
    }
}