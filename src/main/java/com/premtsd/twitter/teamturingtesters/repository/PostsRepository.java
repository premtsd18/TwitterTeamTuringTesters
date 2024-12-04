package com.premtsd.twitter.teamturingtesters.repository;

import com.premtsd.twitter.teamturingtesters.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostsRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long userId);
}
