package com.app.us_twogether.controller;

import com.app.us_twogether.domain.space.AccessLevel;
import com.app.us_twogether.domain.space.SpaceDTO;
import com.app.us_twogether.domain.space.SpaceWithUsersDTO;
import com.app.us_twogether.domain.user.User;
import com.app.us_twogether.service.SpaceService;
import com.app.us_twogether.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${app.api.base-url}/spaces")
public class SpaceController {

    @Autowired
    private SpaceService spaceService;

    @Autowired
    private UserService userService;

    @GetMapping("/shared")
    public ResponseEntity<SpaceDTO> getSharedLink() {

        UsernamePasswordAuthenticationToken authentication = (org.springframework.security.authentication.UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        User user = findUserByAuthentication(authentication);

        SpaceDTO spaceTokenShared = spaceService.getSharedLink(user);

        return ResponseEntity.ok(spaceTokenShared);
    }

    @GetMapping()
    public ResponseEntity<SpaceWithUsersDTO> getSpaceUser(){
        UsernamePasswordAuthenticationToken authentication = (org.springframework.security.authentication.UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        User user = findUserByAuthentication(authentication);

        SpaceWithUsersDTO space = spaceService.getSpaceWithUsers(user);

        return ResponseEntity.ok(space);
    }

    @PostMapping
    public ResponseEntity<SpaceDTO> createSpace() {
        UsernamePasswordAuthenticationToken authentication = (org.springframework.security.authentication.UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        User user = findUserByAuthentication(authentication);

        SpaceDTO newSpace = spaceService.createSpace(user);

        return ResponseEntity.ok(newSpace);
    }

    @PostMapping("/join/{token}")
    public ResponseEntity<String> joinSpace(@PathVariable String token) {
        UsernamePasswordAuthenticationToken authentication = (org.springframework.security.authentication.UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        User user = findUserByAuthentication(authentication);
        spaceService.addUserToSpaceByToken(token, user, AccessLevel.FAMILY);

        return ResponseEntity.ok("Você foi adicionado ao espaço!");
    }

    @PostMapping("/join/us/{token}")
    public ResponseEntity<String> joinUsSpace(@PathVariable String token) {
        UsernamePasswordAuthenticationToken authentication = (org.springframework.security.authentication.UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        User user = findUserByAuthentication(authentication);

        spaceService.addUserToSpaceByToken(token, user, AccessLevel.US);
        return ResponseEntity.ok("Você foi adicionado ao espaço!");
    }

    @PutMapping("/join/{usernameToUpdate}/{accessLevel}")
    public ResponseEntity<String> changeSpaceUserAccessLevel(@PathVariable String usernameToUpdate, @PathVariable AccessLevel accessLevel) {
        if (accessLevel == AccessLevel.US) {
            //TODO Tratamento para melhorar a Exceção
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Alteração de acesso para Owner só pode ser feito via compartilhamento de link.");
        }

        UsernamePasswordAuthenticationToken authentication = (org.springframework.security.authentication.UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        User userOwner = findUserByAuthentication(authentication);

        spaceService.changeSpaceUserAccessLevel(userOwner, usernameToUpdate, accessLevel);

        return ResponseEntity.ok("Seu nível de acesso foi alterado!");
    }

    @DeleteMapping("/join/{usernameToRemove}")
    public ResponseEntity<String> removeUserFromSpace(@PathVariable String usernameToRemove) {
        UsernamePasswordAuthenticationToken authentication = (org.springframework.security.authentication.UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        User userOwner = findUserByAuthentication(authentication);

        spaceService.removeUserFromSpace(userOwner, usernameToRemove);

        return ResponseEntity.ok("Usuário foi removido do espaço!");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteSpace() {
        UsernamePasswordAuthenticationToken authentication = (org.springframework.security.authentication.UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        User user = findUserByAuthentication(authentication);

        spaceService.deleteSpace(user);

        return ResponseEntity.ok("Espaço deletado com sucesso!");
    }

    private User findUserByAuthentication(UsernamePasswordAuthenticationToken authentication) {
        return userService.findByUsername(authentication.getName());
    }
}
