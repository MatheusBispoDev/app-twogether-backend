package com.app.us_twogether.repository;

import com.app.us_twogether.domain.space.AccessLevel;
import com.app.us_twogether.domain.space.Space;
import com.app.us_twogether.domain.space.UserAccessDTO;
import com.app.us_twogether.domain.space.UserSpaceRole;
import com.app.us_twogether.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSpaceRoleRepository extends JpaRepository<UserSpaceRole, Long> {
    boolean existsByUserAndSpace(User user, Space space);
    boolean existsByUserAndAccessLevel(User user, AccessLevel accessLevel);
    Optional<UserSpaceRole> findByUserAndSpace(User user, Space space);

    @Query("SELECT COUNT(usr) FROM UserSpaceRole usr WHERE usr.space.spaceId = :spaceId AND usr.accessLevel = :accessLevel")
    Integer countUsersBySpaceAndRole(@Param("spaceId") Long spaceId, @Param("accessLevel") AccessLevel accessLevel);

    @Query("SELECT new com.app.us_twogether.domain.space.UserAccessDTO(usr.username, usr.email, usrSpace.accessLevel) " +
            "FROM UserSpaceRole usrSpace " +
            "JOIN User usr ON usr.username = usrSpace.user.username " +
            "WHERE usrSpace.space.spaceId = :spaceId")
    Optional<List<UserAccessDTO>> findUsersBySpaceId(@Param("spaceId") Long spaceId);
}
