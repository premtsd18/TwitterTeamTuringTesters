package com.premtsd.twitter.teamturingtesters.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.premtsd.twitter.teamturingtesters.dto.*;
import com.premtsd.twitter.teamturingtesters.entity.Role;
import com.premtsd.twitter.teamturingtesters.entity.User;
import com.premtsd.twitter.teamturingtesters.exception.BadRequestException;
import com.premtsd.twitter.teamturingtesters.exception.ResourceNotFoundException;
import com.premtsd.twitter.teamturingtesters.repository.RoleRepository;
import com.premtsd.twitter.teamturingtesters.repository.UserRepository;
import com.premtsd.twitter.teamturingtesters.repository.UserWithFollowerCountsProjection;
import com.premtsd.twitter.teamturingtesters.utils.PasswordUtil;
import com.premtsd.twitter.teamturingtesters.utils.dependencyInversonPrinciple.StreamingMessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    private StreamingMessageSender streamingMessageSender;


    public UserDto signUp(SignupRequestDto signupRequestDto) {
        boolean exists = userRepository.existsByEmail(signupRequestDto.getEmail());
        if(exists) {
            throw new BadRequestException("User already exists, cannot signup again.");
        }

        Set<Role> roleSet=new HashSet<>();
        for(String role:signupRequestDto.getRoles()){
            Optional<Role> optionalRole= roleRepository.findByName(role);
            if(!optionalRole.isPresent()){
                if(role.equals("ADMIN") || role.equals("USER")) {
                    Role newRole = new Role();
                    newRole.setName(role);
                    Role savedRole = roleRepository.save(newRole);
                }
                throw new BadRequestException("Role does not exist. - " + role);
            }
            roleSet.add(optionalRole.get());
        }

        User user = modelMapper.map(signupRequestDto, User.class);
        user.setRoles(roleSet);
        user.setPassword(PasswordUtil.hashPassword(signupRequestDto.getPassword()));

        User savedUser = userRepository.save(user);
        if(savedUser != null) {
            SendEmailEventDto sendEmailEventDto=new SendEmailEventDto();
            sendEmailEventDto.setTo(savedUser.getEmail());
            sendEmailEventDto.setSubject("Your account has been created at TuringX");
            sendEmailEventDto.setBody("Hi "+savedUser.getName()+",\n"+" Thanks for signing up");
            streamingMessageSender.sendMessage(sendEmailEventDto);
        }
        return mapUserToUserDto(savedUser);
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        // Fetch user by email
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + loginRequestDto.getEmail()));

        // Check password
        boolean isPasswordMatch = PasswordUtil.checkPassword(loginRequestDto.getPassword(), user.getPassword());
        if (!isPasswordMatch) {
            throw new BadRequestException("Incorrect password");
        }

        // Generate token
        String token = jwtService.generateAccessToken(user);
        Long userId = jwtService.getUserIdFromToken(token);

        // Fetch user with follower counts using projection
        UserWithFollowerCountsProjection userProjection = userRepository.findUserWithFollowerCounts(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Build the LoginResponseDto
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setToken(token);
        loginResponseDto.setUserId(userProjection.getId());
        loginResponseDto.setName(userProjection.getName());
        loginResponseDto.setEmail(userProjection.getEmail());
        loginResponseDto.setProfileBio(userProjection.getProfileBio());
        loginResponseDto.setFollowersCount(userProjection.getFollowersCount());
        loginResponseDto.setFollowingCount(userProjection.getFollowingCount());

        return loginResponseDto;
    }



    private UserDto mapUserToUserDto(User user) {
        // Map roles to role names
        List<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        // Create UserDto and map basic user information
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setRoles(roleNames);

        // Map only the first level of followers
        userDto.setFollowerList(user.getFollowerList().stream()
                .map(follower -> {
                    UserDto followerDto = new UserDto();
                    followerDto.setId(follower.getId());
                    followerDto.setName(follower.getName());
                    followerDto.setEmail(follower.getEmail());
                    return followerDto;
                })
                .collect(Collectors.toSet()));

        return userDto;
    }

    public List<UserDto> getAllUsers(){
        return userRepository.findAll().stream().map(user -> mapUserToUserDto(user)).collect(Collectors.toList());
    }

}

