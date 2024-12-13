package com.premtsd.twitter.teamturingtesters.service;

import com.premtsd.twitter.teamturingtesters.dto.ConnectionFollowerResponseDto;
import com.premtsd.twitter.teamturingtesters.dto.PostCreateRequestDto;
import com.premtsd.twitter.teamturingtesters.dto.PostDto;
import com.premtsd.twitter.teamturingtesters.dto.UserDto;
import com.premtsd.twitter.teamturingtesters.entity.Post;
import com.premtsd.twitter.teamturingtesters.entity.User;
import com.premtsd.twitter.teamturingtesters.exception.ResourceNotFoundException;
import com.premtsd.twitter.teamturingtesters.repository.PostsRepository;
import com.premtsd.twitter.teamturingtesters.repository.UserRepository;
import com.premtsd.twitter.teamturingtesters.utils.Observer;
import com.premtsd.twitter.teamturingtesters.utils.ObserverManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostsService {

    private final PostsRepository postsRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final ObserverManager observerManager;

    public PostDto createPost(PostCreateRequestDto postDto, Long userId) {
        Post post = modelMapper.map(postDto, Post.class);
        post.setUserId(userId);
        post.setCreatedAt(LocalDateTime.now());

        post.setId(null);
        Post savedPost = postsRepository.save(post);
        ArrayList<ConnectionFollowerResponseDto> connectionFollowersList = (ArrayList<ConnectionFollowerResponseDto>) getFollowerPost(postDto.getUserId());
        ArrayList<UserDto> followerUserIdList= new ArrayList<>();

        String notification = "User " + postDto.getUserId() + " posted: " + postDto.getContent();

        for (ConnectionFollowerResponseDto connectionFollowerResponseDto : connectionFollowersList) {
            followerUserIdList.add(connectionFollowerResponseDto.getUser());
            for(Observer observer:observerManager.getObservers()) {
                observer.notify(connectionFollowerResponseDto.getUser(),notification);
            }
        }
        // Create a notification message

        // Notify all followers
//        notificationService.sendNotificationToFollowers(followerUserIdList, notification);

        return modelMapper.map(savedPost, PostDto.class);
    }

    public PostDto getPostById(Long postId) {
//        log.debug("Retrieving post with ID: {}", postId);

//        Long userId = UserContextHolder.getCurrentUserId();

//        List<PersonDto> firstConnections = connectionsClient.getFirstConnections();

//        TODO send Notifications to all connections

        Post post = postsRepository.findById(postId).orElseThrow(() ->
                new ResourceNotFoundException(" Post not found withid: "+postId));
        return modelMapper.map(post, PostDto.class);
    }

    public List<PostDto> getAllPostsOfUser(Long userId) {
        List<Post> posts = postsRepository.findByUserId(userId);

        return posts
            .stream()
            .map((element) -> modelMapper.map(element, PostDto.class))
            .collect(Collectors.toList());
    }

    public List<ConnectionFollowerResponseDto> getFollowerPost(Long userId) throws ResourceNotFoundException {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) throw  new ResourceNotFoundException("User not found with id - "+userId);
        ArrayList<ConnectionFollowerResponseDto> connectionFollowerResponseDtoList= mapFollowerAndPosts(user.get());
        return connectionFollowerResponseDtoList;
    }

    private ArrayList<ConnectionFollowerResponseDto> mapFollowerAndPosts(User currUser){
        ArrayList<ConnectionFollowerResponseDto> connectionFollowerResponseDtoList= new ArrayList<>();
        for(User user: currUser.getFollowerList()){
            ConnectionFollowerResponseDto connectionFollowerResponseDto = new ConnectionFollowerResponseDto();
//            System.out.println(user.getClass());
            connectionFollowerResponseDto.setUser(mapUserToUserDto(user));
            getAllPostsOfUser(user.getId());
            List<PostDto> postDtoList =  getAllPostsOfUser(user.getId());
            connectionFollowerResponseDto.setPosts((ArrayList<PostDto>) postDtoList);
            connectionFollowerResponseDtoList.add(connectionFollowerResponseDto);
        }
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
