package com.premtsd.twitter.teamturingtesters.dto;

import lombok.Data;

@Data
public class LoginResponseDto {
    private String token;
    private Long userId;
}
