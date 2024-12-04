package com.premtsd.twitter.teamturingtesters.controller;

import com.premtsd.twitter.teamturingtesters.dto.LoginRequestDto;
import com.premtsd.twitter.teamturingtesters.dto.LoginResponseDto;
import com.premtsd.twitter.teamturingtesters.dto.SignupRequestDto;
import com.premtsd.twitter.teamturingtesters.dto.UserDto;
import com.premtsd.twitter.teamturingtesters.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignupRequestDto signupRequestDto) {
        UserDto userDto = authService.signUp(signupRequestDto);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        String token = authService.login(loginRequestDto);
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setToken(token);
        loginResponseDto.setUserId(-1);
        return ResponseEntity.ok(loginResponseDto);
    }
}
