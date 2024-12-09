package com.premtsd.twitter.teamturingtesters.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class NotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Send notification to a specific user
    public void sendNotificationToUser(Long userId, String message) {
        messagingTemplate.convertAndSendToUser( String.valueOf(userId), "/queue/notifications", message);
    }

    // Send notification to multiple users (followers)
    public void sendNotificationToFollowers(ArrayList<Long> followerIdList, String message) {
        for (Long followerId : followerIdList) {
            sendNotificationToUser(followerId, message);
        }
    }
}