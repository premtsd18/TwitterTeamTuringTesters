package com.premtsd.twitter.teamturingtesters.controller;


import com.premtsd.twitter.teamturingtesters.dto.ConnectionFollowerResponseDto;
import com.premtsd.twitter.teamturingtesters.dto.NotificationDto;
import com.premtsd.twitter.teamturingtesters.dto.PostCreateRequestDto;
import com.premtsd.twitter.teamturingtesters.dto.PostDto;
import com.premtsd.twitter.teamturingtesters.entity.Notification;
import com.premtsd.twitter.teamturingtesters.exception.BadRequestException;
import com.premtsd.twitter.teamturingtesters.service.ConnectionService;
import com.premtsd.twitter.teamturingtesters.service.NotificationService;
import com.premtsd.twitter.teamturingtesters.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostsController {

    private final PostsService postsService;
    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostCreateRequestDto postDto) {
        if(postDto.getContent().length()>=250) throw new BadRequestException("Content is soooo huge");
        PostDto createdPost;
            createdPost = postsService.createPost(postDto, postDto.getUserId());



        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPost(@PathVariable Long postId) {
        PostDto postDto = postsService.getPostById(postId);
        return ResponseEntity.ok(postDto);
    }

    @GetMapping("/users/{userId}/allPosts")
    public ResponseEntity<List<PostDto>> getAllPostsOfUser(@PathVariable Long userId) {
        List<PostDto> posts = postsService.getAllPostsOfUser(userId);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/users/{userId}/allNotifications")
    public ResponseEntity<List<NotificationDto>> getAllNotificationsOfUser(@PathVariable Long userId) {
        List<NotificationDto> notificationList=notificationService.getAllPendingNotification(userId);
        return ResponseEntity.ok(notificationList);
    }


}
