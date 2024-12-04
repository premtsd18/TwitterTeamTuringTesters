package com.premtsd.twitter.teamturingtesters.controller;

import com.premtsd.twitter.teamturingtesters.dto.ConnectionRequestDto;
import com.premtsd.twitter.teamturingtesters.dto.PostCreateRequestDto;
import com.premtsd.twitter.teamturingtesters.dto.PostDto;
import com.premtsd.twitter.teamturingtesters.service.ConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/connection")
public class ConnectionController {
    private final ConnectionService connectionService;


    @PostMapping
    public ResponseEntity<Void> followUser(@RequestBody ConnectionRequestDto connectionRequestDto) {
        connectionService.follow(connectionRequestDto.getUserId1(),connectionRequestDto.getUserId2());
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

}
