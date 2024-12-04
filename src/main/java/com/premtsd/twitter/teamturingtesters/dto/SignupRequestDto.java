package com.premtsd.twitter.teamturingtesters.dto;

import lombok.Data;

import java.util.Set;

@Data
public class SignupRequestDto {
    private String name;
    private String email;
    private String password;
    private Set<String> roles;
}
