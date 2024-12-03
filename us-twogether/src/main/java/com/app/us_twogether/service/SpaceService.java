package com.app.us_twogether.service;

import com.app.us_twogether.domain.space.AccessLevel;
import com.app.us_twogether.domain.space.Space;
import com.app.us_twogether.domain.space.UserSpaceRole;
import com.app.us_twogether.domain.user.User;
import com.app.us_twogether.exception.DataAlreadyExistsException;
import com.app.us_twogether.repository.SpaceRepository;
import com.app.us_twogether.repository.UserSpaceRoleRepository;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class SpaceService {

    @Autowired
    private SpaceRepository spaceRepository;

    @Autowired
    private UserSpaceRoleRepository userSpaceRoleRepository;

    @Autowired
    private UserService userService;

    public void createSpace(User creator) {
        String sharedToken = generateSharedSpaceToken();

        Space newSpace = new Space();
        newSpace.setName("US " + creator.getName() + " - TwoGether ");
        newSpace.setSharedToken(sharedToken);

        UserSpaceRole role = new UserSpaceRole();
        role.setUser(creator);
        role.setSpace(newSpace);
        role.setAccessLevel(AccessLevel.US);

        spaceRepository.save(newSpace);
        userSpaceRoleRepository.save(role);
    }

    public String getSharedLink(String username) {
        Optional<User> userInvited = userService.findByUsername(username);
        Space space = userSpaceRoleRepository.findByUserAndAccessLevel(userInvited.get(), AccessLevel.US);

        if (space.getSharedToken() == null) {
            String token = generateSharedSpaceToken();
            space.setSharedToken(token);
            spaceRepository.save(space);
        }

        return "http://localhost:8099/spaces/join/" + space.getSharedToken();
    }

    public SpaceService(SpaceRepository spaceRepository, UserSpaceRoleRepository userSpaceRoleRepository) {
        this.spaceRepository = spaceRepository;
        this.userSpaceRoleRepository = userSpaceRoleRepository;
    }

    public void addUserToSpaceByToken(String token, String username, AccessLevel acessLevel) {
        Optional<User> userInvited = userService.findByUsername(username);

        if (userInvited.isEmpty()) {
            throw new ResourceNotFoundException("Usuário não encontrado");
        }

        Space space = spaceRepository.findBySharedToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Espaço não encontrado para o token fornecido"));

        if (userSpaceRoleRepository.existsByUserAndSpace(userInvited, space)) {
            throw new DataAlreadyExistsException("Usuário já está associado a este espaço");
        }

        UserSpaceRole userSpaceRole = new UserSpaceRole();
        userSpaceRole.setUser(userInvited.get());
        userSpaceRole.setSpace(space);
        userSpaceRole.setAccessLevel(acessLevel);

        userSpaceRoleRepository.save(userSpaceRole);
    }

    private String generateSharedSpaceToken() {
        return UUID.randomUUID().toString();
    }
}
