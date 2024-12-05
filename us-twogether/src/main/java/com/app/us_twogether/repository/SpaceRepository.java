package com.app.us_twogether.repository;

import com.app.us_twogether.domain.space.Space;
import com.app.us_twogether.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpaceRepository extends JpaRepository<Space, Long> {
    Optional<Space> findBySharedToken(String sharedToken);
    @Query("SELECT spc FROM Space spc JOIN UserSpaceRole usr ON spc = usr.space WHERE usr.user = :user AND usr.accessLevel = 'US'")
    Optional<Space> findByUserOwnerSpace(@Param("user") User user);
}
