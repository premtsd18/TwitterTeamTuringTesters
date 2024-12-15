package com.premtsd.twitter.teamturingtesters.utils.observerPattern;

import com.premtsd.twitter.teamturingtesters.dto.UserDto;


public interface Notifier {
    void notify(UserDto user, String message);
}
