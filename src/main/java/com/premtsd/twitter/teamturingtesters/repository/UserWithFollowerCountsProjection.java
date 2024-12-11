package com.premtsd.twitter.teamturingtesters.repository;

public interface UserWithFollowerCountsProjection {
    Long getId();
    String getName();
    String getEmail();
    String getProfileBio();
    Long getFollowersCount();
    Long getFollowingCount();
}