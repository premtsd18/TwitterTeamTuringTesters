package com.premtsd.twitter.teamturingtesters.dto;

import com.premtsd.twitter.teamturingtesters.entity.User;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private List<String> roles;
    private Set<UserDto> followerList;
}
