package com.premtsd.twitter.teamturingtesters.utils;

import com.premtsd.twitter.teamturingtesters.dto.UserDto;
import com.premtsd.twitter.teamturingtesters.entity.User;
import org.springframework.stereotype.Component;

@Component
public class TerminalNotify implements Observer{
    @Override
    public void notify(UserDto user, String message) {
        System.out.println("Terminal Output:\n Notification for "+user.getName()+" - "+message);
    }
}
