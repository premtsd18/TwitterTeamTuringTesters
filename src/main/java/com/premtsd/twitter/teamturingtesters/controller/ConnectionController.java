package com.premtsd.twitter.teamturingtesters.controller;

import com.premtsd.twitter.teamturingtesters.dto.*;
import com.premtsd.twitter.teamturingtesters.service.ConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RequiredArgsConstructor
@RestController
@RequestMapping("/connection")
public class ConnectionController {
    private final ConnectionService connectionService;


    @PostMapping("/follow")
    public ResponseEntity<Void> followUser(@RequestBody ConnectionRequestDto connectionRequestDto) {
        connectionService.follow(connectionRequestDto.getUserId1(),connectionRequestDto.getUserId2());
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @GetMapping("/getAllPosts")
    public ResponseEntity<ArrayList<ConnectionFollowerResponseDto>> getFollowerPost(@RequestBody ConnectionFollowerRequestDto connectionFollowerRequestDto) {


        ArrayList<ConnectionFollowerResponseDto> connectionFollowersList = (ArrayList<ConnectionFollowerResponseDto>) connectionService.getFollowerPost(connectionFollowerRequestDto.getUserId());
        return new ResponseEntity<>(connectionFollowersList, HttpStatus.OK);
    }


}
