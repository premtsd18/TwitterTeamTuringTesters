package com.premtsd.twitter.teamturingtesters.utils;

import com.premtsd.twitter.teamturingtesters.dto.UserDto;
import com.premtsd.twitter.teamturingtesters.entity.User;


public interface Observer {
    void notify(UserDto user, String message);
}
