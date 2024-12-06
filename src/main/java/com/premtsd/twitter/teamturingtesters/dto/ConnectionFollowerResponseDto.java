package com.premtsd.twitter.teamturingtesters.dto;

import lombok.Data;

import java.util.ArrayList;

@Data
public class ConnectionFollowerResponseDto {
    UserDto user;
    ArrayList<PostDto> posts;
}
