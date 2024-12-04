package com.premtsd.twitter.teamturingtesters.dto;

import lombok.Data;

@Data
public class PostCreateRequestDto {
    private String content;
    private long userId;
}
