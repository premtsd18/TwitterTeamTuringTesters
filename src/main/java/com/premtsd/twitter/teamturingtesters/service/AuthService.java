package com.premtsd.twitter.teamturingtesters.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.premtsd.twitter.teamturingtesters.dto.LoginRequestDto;
import com.premtsd.twitter.teamturingtesters.dto.SendEmailEventDto;
import com.premtsd.twitter.teamturingtesters.dto.SignupRequestDto;
import com.premtsd.twitter.teamturingtesters.dto.UserDto;
import com.premtsd.twitter.teamturingtesters.entity.Role;
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

    public String login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: "+loginRequestDto.getEmail()));

        boolean isPasswordMatch = PasswordUtil.checkPassword(loginRequestDto.getPassword(), user.getPassword());

        if(!isPasswordMatch) {
            throw new BadRequestException("Incorrect password");
        }

        return jwtService.generateAccessToken(user);
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

        return userDto;
    }
}

