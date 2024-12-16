package com.premtsd.twitter.teamturingtesters.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.premtsd.twitter.teamturingtesters.dto.ConnectionFollowerResponseDto;
import com.premtsd.twitter.teamturingtesters.dto.LoginRequestDto;
import com.premtsd.twitter.teamturingtesters.dto.PostDto;
import com.premtsd.twitter.teamturingtesters.dto.UserDto;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    private final PostsService postsService;

    public void follow(long userId1,long userId2) {

        if(userId1==userId2) throw new BadRequestException("Self follow not Allowed");

        User user1 = userRepository.findById(userId1).orElseThrow(() ->
                new ResourceNotFoundException("User not found with id: "+userId1));

        User user2 = userRepository.findById(userId2).orElseThrow(() ->
                new ResourceNotFoundException("User not found with id: "+userId2));

        if(user1.getFollowerList().contains(user2)){
            user1.getFollowerList().remove(user2);
        }
        else{
            user1.getFollowerList().add(user2);
        }
        userRepository.save(user1);

    }

    public List<ConnectionFollowerResponseDto> getFollowerPost(Long userId) throws ResourceNotFoundException {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) throw  new ResourceNotFoundException("User not found with id - "+userId);
        ArrayList<ConnectionFollowerResponseDto> connectionFollowerResponseDtoList= mapFollowerAndPosts(user.get());
        return connectionFollowerResponseDtoList;
    }


    private ArrayList<ConnectionFollowerResponseDto> mapFollowerAndPosts(User currUser){
        ArrayList<ConnectionFollowerResponseDto> connectionFollowerResponseDtoList= new ArrayList<>();
        System.out.println(currUser);
        for(User user: currUser.getFollowerList()){
            ConnectionFollowerResponseDto connectionFollowerResponseDto = new ConnectionFollowerResponseDto();
//            System.out.println(user.getClass());
            connectionFollowerResponseDto.setUser(mapUserToUserDto(user));
            postsService.getAllPostsOfUser(user.getId());
            List<PostDto> postDtoList =  postsService.getAllPostsOfUser(user.getId());
            connectionFollowerResponseDto.setPosts((ArrayList<PostDto>) postDtoList);
            connectionFollowerResponseDtoList.add(connectionFollowerResponseDto);
        }

        ConnectionFollowerResponseDto connectionFollowerResponseDto = new ConnectionFollowerResponseDto();
//            System.out.println(user.getClass());
        connectionFollowerResponseDto.setUser(mapUserToUserDto(currUser));
        postsService.getAllPostsOfUser(currUser.getId());
        List<PostDto> postDtoList =  postsService.getAllPostsOfUser(currUser.getId());
        connectionFollowerResponseDto.setPosts((ArrayList<PostDto>) postDtoList);
        connectionFollowerResponseDtoList.add(connectionFollowerResponseDto);

        return connectionFollowerResponseDtoList;
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