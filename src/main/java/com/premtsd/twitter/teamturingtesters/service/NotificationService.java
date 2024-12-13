package com.premtsd.twitter.teamturingtesters.service;

import com.premtsd.twitter.teamturingtesters.dto.NotificationDto;
import com.premtsd.twitter.teamturingtesters.dto.UserDto;
import com.premtsd.twitter.teamturingtesters.entity.Notification;
import com.premtsd.twitter.teamturingtesters.entity.User;
import com.premtsd.twitter.teamturingtesters.repository.NotificationRepository;
import com.premtsd.twitter.teamturingtesters.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    public void sendNotification(UserDto userDto, String content) {
        Notification notification = new Notification();
        notification.setUser(userDtoToUser(userDto)); // Map DTO to User entity
        notification.setSent(false);
        notification.setText(content);
        notificationRepository.save(notification);
    }

    public User userDtoToUser(UserDto userDto) {
        return userRepository.findById(userDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userDto.getId()));
    }

    public List<NotificationDto> getAllPendingNotification(long userId) {
        // Retrieve only unsent notifications for the given user
        List<Notification> notificationList = notificationRepository.findByUser_Id(userId).stream()
                .filter(notification -> !notification.isSent()) // Filter out already sent notifications
                .collect(Collectors.toList());

        List<NotificationDto> notificationDtoList = new ArrayList<>();
        // Update notifications to mark them as sent
        notificationList.forEach(notification -> {
            notification.setSent(true);
            notificationRepository.save(notification); // Save updated notification
            NotificationDto notificationDto = new NotificationDto();
            notificationDto.setId(notification.getId());
            notificationDto.setText(notification.getText());
            notificationDto.setUserName(notification.getUser().getName());
            notificationDtoList.add(notificationDto);
        });

        return notificationDtoList; // Return filtered and updated notifications
    }
}