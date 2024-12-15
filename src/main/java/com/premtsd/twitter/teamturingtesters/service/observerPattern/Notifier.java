package com.premtsd.twitter.teamturingtesters.service.observerPattern;

import com.premtsd.twitter.teamturingtesters.dto.UserDto;


public interface Notifier {
    void notify(UserDto user, String message);
}
