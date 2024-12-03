package com.app.us_twogether.repository;

import com.app.us_twogether.domain.space.AccessLevel;
import com.app.us_twogether.domain.space.Space;
import com.app.us_twogether.domain.space.UserSpaceRole;
import com.app.us_twogether.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSpaceRoleRepository extends JpaRepository<UserSpaceRole, Integer> {
    boolean existsByUserAndSpace(Optional<User> user, Space space);
    Space findByUserAndAccessLevel(User user, AccessLevel accessLevel);
}
