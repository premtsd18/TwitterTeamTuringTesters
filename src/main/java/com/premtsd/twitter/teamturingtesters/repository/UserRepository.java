package com.premtsd.twitter.teamturingtesters.repository;

import com.premtsd.twitter.teamturingtesters.dto.UserWithFollowerCountsDto;
import com.premtsd.twitter.teamturingtesters.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    @Query(value = "SELECT u.id AS id, u.name AS name, u.email AS email, u.profile_bio AS profileBio, " +
            " (SELECT COUNT(1) FROM user_followers WHERE user_id = :id) AS followersCount, " +
            " (SELECT COUNT(1) FROM user_followers WHERE follower_id = :id) AS followingCount " +
            " FROM users u WHERE u.id = :id", nativeQuery = true)
    Optional<UserWithFollowerCountsProjection> findUserWithFollowerCounts(@Param("id") Long id);

}
