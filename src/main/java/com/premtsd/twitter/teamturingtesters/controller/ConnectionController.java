package com.premtsd.twitter.teamturingtesters.controller;

import com.premtsd.twitter.teamturingtesters.dto.*;
import com.premtsd.twitter.teamturingtesters.service.ConnectionService;
import com.premtsd.twitter.teamturingtesters.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
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
    private final JwtService jwtService;


    @PostMapping("/follow")
    public ResponseEntity<Void> followUser(@RequestBody ConnectionRequestDto connectionRequestDto) {
        connectionService.follow(connectionRequestDto.getUserId1(),connectionRequestDto.getUserId2());
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @GetMapping("/getAllPosts")
    public ResponseEntity<ArrayList<ConnectionFollowerResponseDto>> getFollowerPost( HttpServletRequest httpServletRequest) {
        String authTokenHeader = httpServletRequest.getHeader("Authorization");
        authTokenHeader=authTokenHeader.split("Bearer ")[1];
        ArrayList<ConnectionFollowerResponseDto> connectionFollowersList = (ArrayList<ConnectionFollowerResponseDto>) connectionService.getFollowerPost(jwtService.getUserIdFromToken(authTokenHeader));
        return new ResponseEntity<>(connectionFollowersList, HttpStatus.OK);
    }


}
