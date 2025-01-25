package com.app.us_twogether.domain.space;

import com.app.us_twogether.domain.user.User;
import com.app.us_twogether.domain.user.UserService;
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

    @PostMapping
    public ResponseEntity<SpaceResponseDTO> createSpace() {
        UsernamePasswordAuthenticationToken authentication = (org.springframework.security.authentication.UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        User user = findUserByAuthentication(authentication);

        SpaceResponseDTO newSpace = spaceService.createSpace(user);

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

    @PutMapping("/{spaceId}/join/{usernameToUpdate}/{accessLevel}")
    public ResponseEntity<String> changeSpaceUserAccessLevel(@PathVariable Long spaceId, @PathVariable String usernameToUpdate, @PathVariable AccessLevel accessLevel) {
        if (accessLevel == AccessLevel.US) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Alteração de acesso para Owner só pode ser feito via compartilhamento de link.");
        }

        spaceService.changeSpaceUserAccessLevel(spaceId, usernameToUpdate, accessLevel);

        return ResponseEntity.ok("Nível de acesso foi alterado!");
    }

    @GetMapping("/{spaceId}/shared")
    public ResponseEntity<SpaceResponseDTO> getSharedLink(@PathVariable Long spaceId) {
        SpaceResponseDTO spaceTokenShared = spaceService.getSharedLink(spaceId);

        return ResponseEntity.ok(spaceTokenShared);
    }

    @GetMapping("/{spaceId}")
    public ResponseEntity<SpaceWithUsersDTO> getSpaceUser(@PathVariable Long spaceId){
        SpaceWithUsersDTO space = spaceService.getSpaceWithUsers(spaceId);

        return ResponseEntity.ok(space);
    }

    @PutMapping("/{spaceId}")
    public ResponseEntity<SpaceResponseDTO> updateSpace(@PathVariable Long spaceId, @RequestParam String spaceName){
        SpaceResponseDTO space = spaceService.updatedSpace(spaceId, spaceName);

        return ResponseEntity.ok(space);
    }

    @DeleteMapping("/{spaceId}/join/{usernameToRemove}")
    public ResponseEntity<String> removeUserFromSpace(@PathVariable Long spaceId, @PathVariable String usernameToRemove) {
        spaceService.removeUserFromSpace(spaceId, usernameToRemove);

        return ResponseEntity.ok("Usuário foi removido do espaço!");
    }

    @DeleteMapping("/{spaceId}")
    public ResponseEntity<String> deleteSpace(@PathVariable Long spaceId) {
        spaceService.deleteSpace(spaceId);

        return ResponseEntity.ok("Espaço deletado com sucesso!");
    }

    private User findUserByAuthentication(UsernamePasswordAuthenticationToken authentication) {
        return userService.getUser(authentication.getName());
    }
}
