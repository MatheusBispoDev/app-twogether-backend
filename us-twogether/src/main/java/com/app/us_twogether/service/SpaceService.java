package com.app.us_twogether.service;

import com.app.us_twogether.domain.space.*;
import com.app.us_twogether.domain.user.User;
import com.app.us_twogether.exception.DataAlreadyExistsException;
import com.app.us_twogether.repository.SpaceRepository;
import com.app.us_twogether.repository.UserSpaceRoleRepository;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public SpaceDTO createSpace(User creator) {
        if (validateAccessLevelUserUS(creator)) {
            throw new DataAlreadyExistsException("Usuário '" + creator.getUsername() + "' já possue um Espaço criado.");
        }

        String sharedToken = generateSharedSpaceToken();

        Space newSpace = new Space();
        newSpace.setName(creator.getName());
        newSpace.setSharedToken(sharedToken);

        UserSpaceRole role = new UserSpaceRole();
        role.setUser(creator);
        role.setSpace(newSpace);
        role.setAccessLevel(AccessLevel.US);

        spaceRepository.save(newSpace);
        userSpaceRoleRepository.save(role);

        return new SpaceDTO(newSpace.getName(), newSpace.getSharedToken());
    }

    public SpaceDTO getSharedLink(User user) {
        Space space = findSpaceByUser(user);

        if (space.getSharedToken().isEmpty()) {
            String token = generateSharedSpaceToken();
            space.setSharedToken(token);
            spaceRepository.save(space);
        }

        String sharedToken = endpoint + baseUrl + "/spaces/join/" + space.getSharedToken();

        return new SpaceDTO(space.getName(), sharedToken);
    }

    public void addUserToSpaceByToken(String token, User userInvited, AccessLevel acessLevel) {
        Space space = spaceRepository.findBySharedToken(token).orElseThrow(() -> new ResourceNotFoundException("Espaço não encontrado para o Token fornecido"));

        if (userSpaceRoleRepository.existsByUserAndSpace(userInvited, space)) {
            throw new DataAlreadyExistsException("Usuário já está associado a este espaço");
        }

        Integer usersWithAcessUs = userSpaceRoleRepository.countUsersBySpaceAndRole(space.getSpaceId(), AccessLevel.US);

        if (acessLevel == AccessLevel.US && usersWithAcessUs >= 2) {
            throw new DataAlreadyExistsException("O limite de usuários como Owner já foi atingido");
        }

        UserSpaceRole userSpaceRole = new UserSpaceRole();
        userSpaceRole.setUser(userInvited);
        userSpaceRole.setSpace(space);
        userSpaceRole.setAccessLevel(acessLevel);

        userSpaceRoleRepository.save(userSpaceRole);
        space.setName("US " + space.getName() + " " + userInvited.getName() + " - TwoGether");
    }

    public void changeSpaceUserAccessLevel(User userOwner, String usernameToUpdate, AccessLevel acessLevel) {
        Space space = findSpaceByUser(userOwner);

        User userToUpdate = findUserByUsername(usernameToUpdate);

        UserSpaceRole userSpaceRole = findUserRoleByUserAndSpace(userToUpdate, space);

        userSpaceRole.setAccessLevel(acessLevel);

        userSpaceRoleRepository.save(userSpaceRole);
    }

    public void removeUserFromSpace(User userOwner, String usernameToRemove) {
        Space space = findSpaceByUser(userOwner);

        User userToRemove = findUserByUsername(usernameToRemove);

        UserSpaceRole userSpaceRole = findUserRoleByUserAndSpace(userToRemove, space);

        userSpaceRoleRepository.delete(userSpaceRole);
    }

    public SpaceWithUsersDTO getSpaceWithUsers(User user) {
        Space space = findSpaceByUser(user);
        List<UserAccessDTO> users = userSpaceRoleRepository.findUsersBySpaceId(space.getSpaceId()).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrados no espaço não encontrado"));

        return new SpaceWithUsersDTO(space.getName(), space.getSharedToken(), users);
    }

    public void deleteSpace(User user) {
        Space space = findSpaceByUser(user);

        spaceRepository.delete(space);
    }

    public Space findSpaceByUser(User user) {
        return spaceRepository.findByUserOwnerSpace(user).orElseThrow(() -> new ResourceNotFoundException("Espaço não encontrado para o usuário fornecido"));
    }

    public UserSpaceRole findUserRoleByUserAndSpace(User user, Space space) {
        return userSpaceRoleRepository.findByUserAndSpace(user, space).orElseThrow(() -> new ResourceNotFoundException("Acesso de usuário não encontrado"));
    }

    private User findUserByUsername(String username) {
        return userService.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

    private String generateSharedSpaceToken() {
        return UUID.randomUUID().toString();
    }

    private boolean validateAccessLevelUserUS(User user) {
        return userSpaceRoleRepository.existsByUserAndAccessLevel(user, AccessLevel.US);
    }
}
