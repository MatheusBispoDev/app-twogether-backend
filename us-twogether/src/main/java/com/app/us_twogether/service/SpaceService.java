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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SpaceService {

    @Autowired
    private SpaceRepository spaceRepository;

    @Autowired
    private UserSpaceRoleRepository userSpaceRoleRepository;

    @Autowired
    private UserService userService;

    @Value("${app.api.base-url}")
    private String baseUrl;

    @Value("${app.api.endpoint}")
    private String endpoint;

    public SpaceService(SpaceRepository spaceRepository, UserSpaceRoleRepository userSpaceRoleRepository) {
        this.spaceRepository = spaceRepository;
        this.userSpaceRoleRepository = userSpaceRoleRepository;
    }

    public void createSpace(User creator) {
        if (validateAccessLevelUserUS(creator)) {
            throw new DataAlreadyExistsException("Usuário '" + creator.getUsername() + "' já possue um Espaço criado.");
        }

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

    public String getSharedLink(User user) {
        Space space = findSpaceByUser(user);

        if (space.getSharedToken().isEmpty()) {
            String token = generateSharedSpaceToken();
            space.setSharedToken(token);
            spaceRepository.save(space);
        }

        return endpoint + baseUrl + "/spaces/join/" + space.getSharedToken();
    }

    public void addUserToSpaceByToken(String token, User userInvited, AccessLevel acessLevel) {
        Space space = spaceRepository.findBySharedToken(token).orElseThrow(() -> new ResourceNotFoundException("Espaço não encontrado para o Token fornecido"));

        if (validateAccessLevelUserUS(userInvited)) {
            throw new DataAlreadyExistsException("Usuário já está associado a este espaço");
        }

        UserSpaceRole userSpaceRole = new UserSpaceRole();
        userSpaceRole.setUser(userInvited);
        userSpaceRole.setSpace(space);
        userSpaceRole.setAccessLevel(acessLevel);

        userSpaceRoleRepository.save(userSpaceRole);
    }

    public void changeSpaceUserAccessLevel(User userOwner, String usernameToUpdate, AccessLevel acessLevel) {
        Space space = findSpaceByUser(userOwner);

        User userToUpdate = userService.findByUsername(usernameToUpdate).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        UserSpaceRole userSpaceRole = userSpaceRoleRepository.findByUserAndSpace(userToUpdate, space).orElseThrow(() -> new ResourceNotFoundException("Acesso de usuário não encontrado"));
        userSpaceRole.setAccessLevel(acessLevel);

        userSpaceRoleRepository.save(userSpaceRole);
    }

    public void removeUserFromSpace(User userOwner, String usernameToRemove) {
        Space space = findSpaceByUser(userOwner);

        User userToRemove = userService.findByUsername(usernameToRemove).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        UserSpaceRole userSpaceRole = userSpaceRoleRepository.findByUserAndSpace(userToRemove, space).orElseThrow(() -> new ResourceNotFoundException("Acesso de usuário não encontrado"));

        userSpaceRoleRepository.delete(userSpaceRole);
    }

    public void deleteSpace(User user) {
        Space space = findSpaceByUser(user);

        spaceRepository.delete(space);
    }

    private String generateSharedSpaceToken() {
        return UUID.randomUUID().toString();
    }

    private Space findSpaceByUser(User user) {
        return spaceRepository.findByUserOwnerSpace(user).orElseThrow(() -> new ResourceNotFoundException("Espaço não encontrado para o usuário fornecido"));
    }

    private boolean validateAccessLevelUserUS(User user) {
        return userSpaceRoleRepository.existsByUserAndAccessLevel(user, AccessLevel.US);
    }
}