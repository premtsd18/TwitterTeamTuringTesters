package com.premtsd.twitter.teamturingtesters.dto;

import lombok.Data;

import java.util.Set;

@Data
public class LoginResponseDto {
    private String token;
    private Long userId;
    private String name;
    private String email;
    private String profileBio;
    private Long followingCount;
    private Long followersCount;
}
