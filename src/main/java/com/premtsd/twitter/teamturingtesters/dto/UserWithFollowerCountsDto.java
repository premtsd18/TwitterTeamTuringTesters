package com.premtsd.twitter.teamturingtesters.dto;

import lombok.Data;

@Data
public class UserWithFollowerCountsDto {
    private Long id;
    private String name;
    private String email;
    private String profileBio;
    private Long followersCount;
    private Long followingCount;
    public UserWithFollowerCountsDto(Long id, String name, String email, String profileBio, Long followersCount, Long followingCount) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profileBio = profileBio;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
    }
}
