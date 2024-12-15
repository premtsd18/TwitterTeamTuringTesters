package com.premtsd.twitter.teamturingtesters.utils.observerPattern;

import com.premtsd.twitter.teamturingtesters.dto.UserDto;
import com.premtsd.twitter.teamturingtesters.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebPageNotify implements Notifier {
    @Autowired
    private  NotificationService notificationService;

    @Override
    public void notify(UserDto user, String message) {
        notificationService.sendNotification(user, message);
    }
}
