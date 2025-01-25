package com.app.us_twogether.domain.space;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpaceRepository extends JpaRepository<Space, Long> {
    Optional<Space> findBySharedToken(String sharedToken);
}
