package com.premtsd.twitter.teamturingtesters.service;

import com.premtsd.twitter.teamturingtesters.dto.UserDto;
import com.premtsd.twitter.teamturingtesters.entity.Notification;
import com.premtsd.twitter.teamturingtesters.entity.User;
import com.premtsd.twitter.teamturingtesters.repository.NotificationRepository;
import com.premtsd.twitter.teamturingtesters.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final NotificationRepository notificationRepository;

    public NotificationService(UserRepository userRepository, NotificationRepository notificationRepository) {
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
    }

    public void sendNotification(UserDto userDto,String content) {
        Notification notification = new Notification();
        notification.setUser(userDtoToUser(userDto));
        notification.setSent(false);
        notification.setText(content);
        notificationRepository.save(notification);
    }
    public User userDtoToUser(UserDto userDto){
        Optional<User> user = userRepository.findById(userDto.getId());
        User user1 = user.get();
        return user1;
    }

    public List<Notification> getAllPendingNotification(long userId){
        List<Notification> notificationList = notificationRepository.findByUser_Id(userId);
        notificationList = notificationList.stream()
                .filter(notification -> !notification.isSent()) // Filter notifications where isSent() is false
                .collect(Collectors.toList()); // Collect the filtered results into a List
        for(Notification notification:notificationList){
            notification.setSent(true);
            Notification savedNotification = notificationRepository.save(notification);
        }
        return notificationList;
    }
}