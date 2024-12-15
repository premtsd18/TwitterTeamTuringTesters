package com.premtsd.twitter.teamturingtesters.utils.observerPattern;

import com.premtsd.twitter.teamturingtesters.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class TerminalNotify implements Notifier {
    @Override
    public void notify(UserDto user, String message) {
        System.out.println("Terminal Output:\n Notification for "+user.getName()+" - "+message);
    }
}
