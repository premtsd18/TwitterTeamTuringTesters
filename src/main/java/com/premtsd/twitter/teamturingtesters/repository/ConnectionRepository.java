package com.premtsd.twitter.teamturingtesters.repository;

import com.premtsd.twitter.teamturingtesters.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnectionRepository extends JpaRepository<Post, Long> {
}
