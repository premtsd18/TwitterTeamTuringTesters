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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;
    private final ObjectMapper objectMapper;


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
            sendEmailEventDto.setSubject("Your account has been created at LinkedIn-Like");
            sendEmailEventDto.setBody("Hi "+savedUser.getName()+",\n"+" Thanks for signing up");
            String str="";
            try {
                 str = objectMapper.writeValueAsString(sendEmailEventDto);
            }catch (Exception e){
                System.out.println("Error in json serialization");
            }

            System.out.println(str);
            kafkaTemplate.send("topic1", str);
            try {
                SendEmailEventDto event = objectMapper.readValue(str, SendEmailEventDto.class);
            }catch (Exception e){
                System.out.println("Error in json deserialization");
            }
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
        // Convert Set<Role> to List<String> of role names
        List<String> roleNames = user.getRoles().stream()
                .map(role -> role.getName())  // Convert Role to String (role name)
                .collect(Collectors.toList());

        // Map the rest of the User properties to UserDto
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setRoles(roleNames);
//        userDto.setFollowerList(new HashSet<>());
//        for(User user1:user.getFollowerList()) {
//            userDto.getFollowerList().add(mapUserToUserDto(user1));
//        }

        return userDto;
    }

    public List<UserDto> getAllUsers(){
        return userRepository.findAll().stream().map(user -> mapUserToUserDto(user)).collect(Collectors.toList());
    }

}

