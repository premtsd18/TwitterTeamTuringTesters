package com.premtsd.twitter.teamturingtesters.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String email;
    private String password;
}
