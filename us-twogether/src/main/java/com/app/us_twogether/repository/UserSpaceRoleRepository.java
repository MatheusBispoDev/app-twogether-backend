package com.app.us_twogether.repository;

import com.app.us_twogether.domain.space.UserSpaceRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSpaceRoleRepository extends JpaRepository<UserSpaceRole, Integer> {
}
