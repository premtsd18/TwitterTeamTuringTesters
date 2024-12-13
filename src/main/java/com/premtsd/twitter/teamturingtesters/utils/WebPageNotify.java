package com.premtsd.twitter.teamturingtesters.utils;

import com.premtsd.twitter.teamturingtesters.dto.UserDto;
import com.premtsd.twitter.teamturingtesters.entity.User;
import com.premtsd.twitter.teamturingtesters.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class WebPageNotify implements Observer{
    @Autowired
    private  NotificationService notificationService;

    @Override
    public void notify(UserDto user, String message) {
        notificationService.sendNotification(user, message);
    }
}
