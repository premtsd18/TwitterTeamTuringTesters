package com.premtsd.twitter.teamturingtesters.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.premtsd.twitter.teamturingtesters.dto.LoginRequestDto;
import com.premtsd.twitter.teamturingtesters.entity.Post;
import com.premtsd.twitter.teamturingtesters.entity.User;
import com.premtsd.twitter.teamturingtesters.exception.BadRequestException;
import com.premtsd.twitter.teamturingtesters.exception.ResourceNotFoundException;
import com.premtsd.twitter.teamturingtesters.repository.RoleRepository;
import com.premtsd.twitter.teamturingtesters.repository.UserRepository;
import com.premtsd.twitter.teamturingtesters.utils.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;

    public void follow(long userId1,long userId2) {

        User user1 = userRepository.findById(userId1).orElseThrow(() ->
                new ResourceNotFoundException("Post not found with id: "+userId1));

        User user2 = userRepository.findById(userId2).orElseThrow(() ->
                new ResourceNotFoundException("Post not found with id: "+userId2));

        if(user1.getFollowerList().contains(user2)){
            user1.getFollowerList().remove(user2);
        }
        else{
            user1.getFollowerList().add(user2);
        }
        userRepository.save(user1);

    }
}