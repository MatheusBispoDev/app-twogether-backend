package com.app.us_twogether.service;

import com.app.us_twogether.domain.space.AccessLevel;
import com.app.us_twogether.domain.space.Space;
import com.app.us_twogether.domain.space.UserSpaceRole;
import com.app.us_twogether.domain.user.User;
import com.app.us_twogether.repository.SpaceRepository;
import com.app.us_twogether.repository.UserSpaceRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SpaceService {

    @Autowired
    private SpaceRepository spaceRepository;

    @Autowired
    private UserSpaceRoleRepository userSpaceRoleRepository;

    public Space createSpace(String name, User creator) {
        String sharedToken = UUID.randomUUID().toString();

        Space newSpace = new Space(name, sharedToken, List.of(creator));

        UserSpaceRole role = new UserSpaceRole();
        role.setUser(creator);
        role.setSpace(newSpace);
        role.setAccessLevel(AccessLevel.US);

        spaceRepository.save(newSpace);
        userSpaceRoleRepository.save(role);

        return newSpace;
    }

    public Optional<Space> getSpaceByToken(String token) {
        return spaceRepository.findBySharedToken(token);
    }
}
