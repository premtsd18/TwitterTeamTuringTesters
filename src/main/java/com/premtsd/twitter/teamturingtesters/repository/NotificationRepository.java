package com.premtsd.twitter.teamturingtesters.repository;

import com.premtsd.twitter.teamturingtesters.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUser_Id(Long userId);
}
