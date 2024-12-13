package com.premtsd.twitter.teamturingtesters.repository;

import com.premtsd.twitter.teamturingtesters.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    public Optional<Role> findByName(String name);
    public boolean existsByName(String name);
}
