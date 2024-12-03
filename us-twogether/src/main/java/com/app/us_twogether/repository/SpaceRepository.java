package com.app.us_twogether.repository;

import com.app.us_twogether.domain.space.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpaceRepository extends JpaRepository<Space, Integer> {
    Optional<Space> findBySharedToken(String sharedToken);
}
